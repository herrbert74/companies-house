package com.babestudios.companyinfouk.companies

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.ext.test
import com.babestudios.companyinfouk.companies.ui.main.MainExecutor
import com.babestudios.companyinfouk.companies.ui.main.MainStore
import com.babestudios.companyinfouk.companies.ui.main.MainStoreFactory
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResultItem
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var mainExecutor: MainExecutor

	private lateinit var mainStore: MainStore

	private val testCoroutineDispatcher = StandardTestDispatcher()

	private val searchHistoryItem = SearchHistoryItem("TUI", "12344", 12L)

	private val searchResult = CompanySearchResult().also {
		it.totalResults = 10
		it.items = mutableListOf(CompanySearchResultItem())
	}

	@Before
	fun setUp() {

		coEvery { companiesHouseRepository.logScreenView(any()) } answers { }
		coEvery { companiesHouseRepository.logSearch("alma") } answers { }
		coEvery { companiesHouseRepository.recentSearches() } answers { listOf(searchHistoryItem) }

		every { companiesHouseRepository.addRecentSearchItem(searchHistoryItem) } answers
			{ arrayListOf(searchHistoryItem) }

		every { companiesHouseRepository.clearAllRecentSearches() } answers { Exception("") }

		coEvery { companiesHouseRepository.searchCompanies(any(), any()) } answers
			{ searchResult }

		mainExecutor = MainExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		mainStore = MainStoreFactory(DefaultStoreFactory(), mainExecutor).create()

	}

	@Test
	fun `when fab clicked in state recent searches then show delete recent searches dialog is called`() = runTest(
		testCoroutineDispatcher
	) {
		val states = mainStore.states.test()
		mainStore.accept(MainStore.Intent.ClearRecentSearches)

		advanceUntilIdle()
		coVerify(exactly = 1) { companiesHouseRepository.clearAllRecentSearches() }
		states.last().filteredSearchResultItems shouldBe listOf()
	}

	@Test
	fun `when searchItemClicked then addRecentSearchItem is called`() = runTest(
		testCoroutineDispatcher
	) {
		val states = mainStore.states.test()
		mainStore.accept(
			MainStore.Intent.SearchItemClicked(
				searchHistoryItem.companyName,
				searchHistoryItem.companyNumber
			)
		)

		advanceUntilIdle()
		coVerify(exactly = 1) { companiesHouseRepository.addRecentSearchItem(searchHistoryItem) }
		states.last().searchHistoryItems.size shouldBe 1
	}

	@Test
	fun `when search then search companies in repo is called`() = runTest(testCoroutineDispatcher) {
		val states = mainStore.states.test()
		mainStore.accept(MainStore.Intent.SetSearchMenuItemExpanded)
		mainStore.accept(MainStore.Intent.SearchQueryChanged("alma"))

		advanceUntilIdle()
		coVerify(exactly = 1) { companiesHouseRepository.searchCompanies(any(), any()) }
		states.last().filteredSearchResultItems shouldBe searchResult.items
	}

	@Test
	fun `when search load more then search companies in repo is called`() = runTest(testCoroutineDispatcher) {
		val states = mainStore.states.test()
		mainStore.accept(MainStore.Intent.SetSearchMenuItemExpanded)
		mainStore.accept(MainStore.Intent.SearchQueryChanged("alma"))

		advanceUntilIdle()
		mainStore.accept(MainStore.Intent.LoadMoreSearch)

		advanceUntilIdle()
		coVerify(exactly = 2) { companiesHouseRepository.searchCompanies(any(), any()) }
		states.last().filteredSearchResultItems shouldBe searchResult.items
	}

}
