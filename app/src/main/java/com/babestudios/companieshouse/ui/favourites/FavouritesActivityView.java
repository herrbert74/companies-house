package com.babestudios.companieshouse.ui.favourites;

import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

interface FavouritesActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showFavourites(SearchHistoryItem[] searchHistoryItems);

	@CallOnMainThread
	void startCompanyActivity(String companyNumber, String companyName);
}