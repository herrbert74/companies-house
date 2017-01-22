package com.babestudios.companyinfouk.ui.favourites;

import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;

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