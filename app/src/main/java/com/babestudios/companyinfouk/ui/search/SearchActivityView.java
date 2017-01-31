package com.babestudios.companyinfouk.ui.search;

import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

import java.util.ArrayList;

interface SearchActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showError();

	@CallOnMainThread
	void showRecentSearches(SearchHistoryItem[] searchHistoryItems);

	@CallOnMainThread
	void startCompanyActivity(String companyNumber, String companyName);

	@CallOnMainThread
	void showCompanySearchResult(CompanySearchResult companySearchResult, boolean isFromOnNext, SearchPresenter.FilterState filterState);

	@CallOnMainThread
	void clearSearchView();

	@CallOnMainThread
	void refreshRecentSearchesAdapter(ArrayList<SearchHistoryItem> searchHistoryItems);

	@CallOnMainThread
	void changeFabImage(SearchPresenter.FabImage type);

	@CallOnMainThread
	void showDeleteRecentSearchesDialog();

	void setFilterOnAdapter(SearchPresenter.FilterState filterState);

	void setDeferredFilterState(SearchPresenter.FilterState filterState);
}