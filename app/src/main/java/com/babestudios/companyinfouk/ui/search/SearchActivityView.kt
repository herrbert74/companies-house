package com.babestudios.companyinfouk.ui.search

import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem

import net.grandcentrix.thirtyinch.TiView
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread

import java.util.ArrayList

interface SearchActivityView : TiView {

	@CallOnMainThread
	fun showProgress()

	@CallOnMainThread
	fun hideProgress()

	@CallOnMainThread
	fun showError()

	@CallOnMainThread
	fun showRecentSearches(searchHistoryItems: ArrayList<SearchHistoryItem>)

	@CallOnMainThread
	fun startCompanyActivity(companyNumber: String, companyName: String)

	@CallOnMainThread
	fun showCompanySearchResult(companySearchResult: CompanySearchResult, isFromOnNext: Boolean, filterState: SearchPresenter.FilterState)

	@CallOnMainThread
	fun clearSearchView()

	@CallOnMainThread
	fun refreshRecentSearchesAdapter(searchHistoryItems: ArrayList<SearchHistoryItem>)

	@CallOnMainThread
	fun changeFabImage(type: SearchPresenter.FabImage)

	@CallOnMainThread
	fun showDeleteRecentSearchesDialog()

	fun setFilterOnAdapter(filterState: SearchPresenter.FilterState)

	fun setInitialFilterState(filterState: SearchPresenter.FilterState)
}