package com.babestudios.companieshouse.ui.search;

import com.babestudios.companieshouse.data.model.search.CompanySearchResult;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

import java.util.ArrayList;

interface SearchActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showRecentSearches(SearchHistoryItem[] searchHistoryItems);

	@CallOnMainThread
	void startCompanyActivity(String companyNumber, String companyName);

	@CallOnMainThread
	void showCompanySearchResult(final CompanySearchResult companySearchResult);

	@CallOnMainThread
	void clearSearchView();

	@CallOnMainThread
	void refreshRecentSearchesAdapter(ArrayList<SearchHistoryItem> searchHistoryItems);

	@CallOnMainThread
	void changeFabImage(SearchPresenter.FabImage type);

	@CallOnMainThread
	void showDeleteRecentSearchesDialog();
}