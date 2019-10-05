package com.babestudios.companyinfouk.ui.search

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.DaggerTestApplicationComponent
import com.babestudios.companyinfouk.TestApplicationModule
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.ui.main.ContentChange
import com.babestudios.companyinfouk.ui.main.MainPresenter
import com.babestudios.companyinfouk.ui.main.MainViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.CompletableSource
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class SearchPresenterTest {

	private lateinit var mainPresenter: MainPresenter

	private val searchHistoryItem = SearchHistoryItem("TUI", "12344", 12L)

	@Before
	fun setUp() {
		val testApplicationComponent = DaggerTestApplicationComponent.builder()
				.testApplicationModule(TestApplicationModule(CompaniesHouseApplication()))
				.build()
		mainPresenter = testApplicationComponent.mainPresenter()
		//whenever(officersPresenter.companiesRepository.getOfficers("0", "0")).thenReturn(Single.just(Officers()))
		val officersViewModel = MainViewModel()
		//officersViewModel.state.value.companyNumber = "0"
		whenever(mainPresenter.companiesRepository.searchCompanies(any(), anyString())).thenReturn(Single.just(CompanySearchResult()))
		val searchHistoryItems = ArrayList<SearchHistoryItem>()
		val searchHistoryItem1 = SearchHistoryItem("TUI", "12345", 12L)
		searchHistoryItems.add(searchHistoryItem)
		searchHistoryItems.add(searchHistoryItem1)
		whenever(mainPresenter.companiesRepository.addRecentSearchItem(searchHistoryItem)).thenReturn(searchHistoryItems)
		mainPresenter.setViewModel(officersViewModel, CompletableSource { })
	}

	@Test
	fun whenFabClickedInStateRecentSearches_thenShowDeleteRecentSearchesDialogIsCalled() {
		mainPresenter.fabMainClicked()
		assert(mainPresenter.viewModel.state.value.contentChange == ContentChange.DELETE_SEARCH_HISTORY)
	}

	@Test
	fun `when searchItemClicked then addRecentSearchItem and StartActivity Is Called`() {
		mainPresenter.searchItemClicked("TUI", "12344")
		//TODO This uses context in Presenter. Create a string provider to get this from strings.xml, but don't rely on context here
		//assert(mainPresenter.viewModel.state.value.contentChange == ContentChange.SEARCH_HISTORY_ITEMS_UPDATED)
		verify(mainPresenter.companiesRepository).addRecentSearchItem(searchHistoryItem)
	}

	@Test
	fun whenSearch_thenDataManagerSearchCompaniesIsCalled() {
		mainPresenter.search("")
		verify(mainPresenter.companiesRepository).searchCompanies(any(), anyString())
	}

	@Test
	fun whenSearchLoadMore_thenDataManagerSearchCompaniesIsCalled() {
		mainPresenter.search("")
		verify(mainPresenter.companiesRepository).searchCompanies(any(), anyString())
	}


}
