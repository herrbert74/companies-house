package com.babestudios.companyinfouk.ui.search

import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.nhaarman.mockitokotlin2.any
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class SearchPresenterTest {

	private var searchPresenter: SearchPresenter? = null

	private var view: SearchActivityView? = null

	private val searchHistoryItem = SearchHistoryItem("TUI", "12344", 12L)

	@Before
	fun setUp() {
		searchPresenter = SearchPresenter(mock(CompaniesRepository::class.java))
		searchPresenter!!.create()
		view = mock(SearchActivityView::class.java)
		searchPresenter!!.attachView(view!!)
		`when`(searchPresenter!!.companiesRepository.searchCompanies(any(), anyString())).thenReturn(Single.just(CompanySearchResult()))
		val searchHistoryItems = ArrayList<SearchHistoryItem>()
		val searchHistoryItem1 = SearchHistoryItem("RUN", "12345", 12L)
		searchHistoryItems.add(searchHistoryItem)
		searchHistoryItems.add(searchHistoryItem1)
		`when`(searchPresenter!!.companiesRepository.addRecentSearchItem(searchHistoryItem)).thenReturn(searchHistoryItems)
	}

	@Test
	fun whenFabClickedInStateRecentSearches_thenShowDeleteRecentSearchesDialogIsCalled() {
		searchPresenter!!.onFabClicked()
		verify<SearchActivityView>(view).showDeleteRecentSearchesDialog()
	}

	@Test
	fun whenFabClickedInStateSearch_thenClearSearchView_andRefreshAdapter_andShowRecentSearchesCalled() {
		searchPresenter!!.search("")
		searchPresenter!!.onFabClicked()
		verify<SearchActivityView>(view).clearSearchView()
		verify<SearchActivityView>(view).refreshRecentSearchesAdapter(any())
		verify<SearchActivityView>(view, times(2)).showRecentSearches(any())
		verify<SearchActivityView>(view, times(2)).changeFabImage(SearchPresenter.FabImage.FAB_IMAGE_RECENT_SEARCH_DELETE)
	}

	@Test
	fun whenGetCompany_thenDataManagerAddRecentSearchItem_andStartActivityIsCalled() {
		searchPresenter!!.getCompany("TUI", "12344")
		verify<SearchActivityView>(view).startCompanyActivity(anyString(), anyString())
		verify(searchPresenter!!.companiesRepository).addRecentSearchItem(searchHistoryItem)
	}

	@Test
	fun whenSearch_thenDataManagerSearchCompaniesIsCalled() {
		searchPresenter!!.search("")
		verify(searchPresenter!!.companiesRepository).searchCompanies(any(), anyString())
	}

	@Test
	fun whenSearchLoadMore_thenDataManagerSearchCompaniesIsCalled() {
		searchPresenter!!.search("")
		verify(searchPresenter!!.companiesRepository).searchCompanies(any(), anyString())
	}


}
