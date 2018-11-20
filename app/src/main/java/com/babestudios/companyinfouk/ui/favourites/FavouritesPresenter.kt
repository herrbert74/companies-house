package com.babestudios.companyinfouk.ui.favourites

import com.babestudios.companyinfouk.data.DataManager
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem

import net.grandcentrix.thirtyinch.TiPresenter

import javax.inject.Inject

class FavouritesPresenter @Inject
constructor(internal var dataManager: DataManager) : TiPresenter<FavouritesActivityView>() {
	lateinit var searchHistoryItems: Array<SearchHistoryItem>

	public override fun onAttachView(view: FavouritesActivityView) {
		super.onAttachView(view)
		searchHistoryItems = dataManager.favourites
		view.showFavourites(searchHistoryItems)
	}

	internal fun getCompany(companyNumber: String, companyName: String) {
		view?.startCompanyActivity(companyNumber, companyName)
	}

	internal fun removeFavourite(favouriteToRemove: SearchHistoryItem) {
		dataManager.removeFavourite(favouriteToRemove)
	}

}
