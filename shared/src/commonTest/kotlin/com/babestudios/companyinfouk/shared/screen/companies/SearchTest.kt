package com.babestudios.companyinfouk.shared.screen.companies

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResultItem
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.shared.screen.main.MainExecutor
import com.babestudios.companyinfouk.shared.screen.main.MainStore
import com.babestudios.companyinfouk.shared.screen.main.MainStoreFactory
import dev.mokkery.answering.calls
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

class SearchTest {

	private val companiesHouseRepository = mock<CompaniesRepository>()

	private lateinit var mainExecutor: MainExecutor

	private lateinit var mainStore: MainStore

	private val testCoroutineDispatcher = StandardTestDispatcher()

	private val searchHistoryItem = SearchHistoryItem("TUI", "12344", 12L)

	private val searchResult = CompanySearchResult().also {
		it.totalResults = 10
		it.items = mutableListOf(CompanySearchResultItem())
	}

	@BeforeTest
	fun setUp() {

		every { companiesHouseRepository.logScreenView(any()) } calls { }
		every { companiesHouseRepository.logSearch("alma") } calls { }
		everySuspend { companiesHouseRepository.recentSearches() } calls { listOf(searchHistoryItem) }

		every { companiesHouseRepository.addRecentSearchItem(searchHistoryItem) } calls
			{ arrayListOf(searchHistoryItem) }

		every { companiesHouseRepository.clearAllRecentSearches() } calls { Exception("") }

		everySuspend { companiesHouseRepository.searchCompanies(any(), any()) } calls
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
		verifySuspend(exactly(1)) { companiesHouseRepository.clearAllRecentSearches() }
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
		verifySuspend(exactly(1)) { companiesHouseRepository.addRecentSearchItem(searchHistoryItem) }
		states.last().searchHistoryItems.size shouldBe 1
	}

	@Test
	fun `when search then search companies in repo is called`() = runTest(testCoroutineDispatcher) {
		val states = mainStore.states.test()
		mainStore.accept(MainStore.Intent.SetSearchMenuItemExpanded)
		mainStore.accept(MainStore.Intent.SearchQueryChanged("alma"))

		advanceUntilIdle()
		verifySuspend(exactly(1)) { companiesHouseRepository.searchCompanies(any(), any()) }
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
		verifySuspend(exactly(2)) { companiesHouseRepository.searchCompanies(any(), any()) }
		states.last().filteredSearchResultItems shouldBe searchResult.items
	}

}
