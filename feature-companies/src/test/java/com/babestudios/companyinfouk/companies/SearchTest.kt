package com.babestudios.companyinfouk.companies

import androidx.lifecycle.SavedStateHandle
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.common.ext.test
import com.babestudios.companyinfouk.companies.ui.main.MainExecutor
import com.babestudios.companyinfouk.companies.ui.main.MainStore
import com.babestudios.companyinfouk.companies.ui.main.MainStoreFactory
import com.babestudios.companyinfouk.data.utils.StringResourceHelper
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResultItem
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class SearchTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private val stringResourceHelper = mockk<StringResourceHelper>()

	private val savedStateHandle = mockk<SavedStateHandle>()

	private lateinit var mainExecutor: MainExecutor

	private lateinit var mainStore: MainStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	private val searchHistoryItem = SearchHistoryItem("TUI", "12344", 12L)

	private val searchResult = CompanySearchResult().also {
		it.totalResults=1
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

		every { stringResourceHelper.getRecentSearchesString() } answers { ("Recent Searches") }

		every { savedStateHandle.set(any(), any<MainStore.State>()) } answers { }

		mainExecutor = MainExecutor(
			companiesHouseRepository,
			stringResourceHelper,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		mainStore = MainStoreFactory(DefaultStoreFactory(), mainExecutor, savedStateHandle).create()

	}

	@Test
	fun `when fab clicked in state recent searches then show delete recent searches dialog is called`() {
		val states = mainStore.states.test()
		mainStore.accept(MainStore.Intent.ClearRecentSearches)
		coVerify(exactly = 1) { companiesHouseRepository.clearAllRecentSearches() }
		states.last().searchHistoryVisitables shouldBe listOf()
	}

	@Test //TODO Move navigation to UI tests
	fun `when searchItemClicked then addRecentSearchItem and StartActivity Is Called`() {
		val states = mainStore.states.test()
		mainStore.accept(
			MainStore.Intent.SearchItemClicked(
				searchHistoryItem.companyName,
				searchHistoryItem.companyNumber
			)
		)
		coVerify(exactly = 1) { companiesHouseRepository.addRecentSearchItem(searchHistoryItem) }
		states.last().searchHistoryVisitables.size shouldBe 2
	}

	@Test
	fun `when search then data manager search companies is called`() {
		val states = mainStore.states.test()
		mainStore.accept(MainStore.Intent.SetSearchMenuItemExpanded)
		mainStore.accept(MainStore.Intent.SearchQueryChanged("alma"))
		coVerify(exactly = 1) { companiesHouseRepository.searchCompanies(any(), any()) }
		states.last().searchResultItems shouldBe searchResult.items
	}

	@Test
	fun `when search load more then data manager search companies is called`() {
		val states = mainStore.states.test()
		mainStore.accept(
			MainStore.Intent.LoadMoreSearch(
				1
			)
		)
		coVerify(exactly = 1) { companiesHouseRepository.searchCompanies(any(), any()) }
		states.last().searchResultItems shouldBe searchResult.items
	}

}
