package com.babestudios.companyinfouk.ui.favourites

import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem

import net.grandcentrix.thirtyinch.TiView
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread

interface FavouritesActivityView : TiView {

	@CallOnMainThread
	fun showProgress()

	@CallOnMainThread
	fun hideProgress()

	@CallOnMainThread
	fun showFavourites(searchHistoryItems: Array<SearchHistoryItem>?)

	@CallOnMainThread
	fun startCompanyActivity(companyNumber: String, companyName: String)
}