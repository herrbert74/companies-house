package com.babestudios.companyinfouk.ui.filinghistory

import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList
import net.grandcentrix.thirtyinch.TiView
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread

interface FilingHistoryActivityView : TiView {

	@get:CallOnMainThread
	val companyNumber: String

	@get:CallOnMainThread
	val filingCategory: String?

	@CallOnMainThread
	fun showProgress()

	@CallOnMainThread
	fun hideProgress()

	@CallOnMainThread
	fun showError()

	@CallOnMainThread
	fun showFilingHistory(filingHistoryList: FilingHistoryList, categoryFilter: FilingHistoryPresenter.CategoryFilter)

	fun setFilterOnAdapter(categoryFilter: FilingHistoryPresenter.CategoryFilter)

	fun setInitialCategoryFilter(categoryFilter: FilingHistoryPresenter.CategoryFilter)
}