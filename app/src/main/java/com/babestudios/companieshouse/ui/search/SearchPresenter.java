package com.babestudios.companieshouse.ui.search;

import android.util.Log;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.search.CompanySearchResult;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;

import net.grandcentrix.thirtyinch.TiPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observer;

public class SearchPresenter extends TiPresenter<SearchActivityView> implements Observer<CompanySearchResult> {

	enum FabImage {
		FAB_IMAGE_RECENT_SEARCH_DELETE,
		FAB_IMAGE_SEARCH_CLOSE
	}

	private enum ShowState {
		RECENT_SEARCHES,
		SEARCH
	}

	private ShowState showState = ShowState.RECENT_SEARCHES;

	DataManager dataManager;

	private String queryText;

	private boolean isFirstStart = true;

	ArrayList<SearchHistoryItem> searchHistoryItems = null;

	@Inject
	public SearchPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		//CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);

	}

	@Override
	protected void onWakeUp() {
		super.onWakeUp();
		if(isFirstStart){
			isFirstStart = false;
			showRecentSearches(true);
		} else {
			getView().clearSearchView();
			if(searchHistoryItems != null) {
				getView().refreshRecentSearchesAdapter(searchHistoryItems);
			}
		}
	}

	/**
	 * This is needed because Activity Transition messes up the lifecycle (onStart and onWakeUp is not always called). Remove if not needed!
	 */
	public void getSearchHistoryItems(){
		if(searchHistoryItems != null) {
			getView().refreshRecentSearchesAdapter(searchHistoryItems);
			searchHistoryItems = null;
		}
	}

	private void showRecentSearches(boolean isFirstStart) {
		getView().showRecentSearches(dataManager.getRecentSearches());
		if(!isFirstStart) {
			getView().changeFabImage(FabImage.FAB_IMAGE_RECENT_SEARCH_DELETE);
		}
		showState = ShowState.RECENT_SEARCHES;
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		Log.d("test", "onError: " + e.fillInStackTrace());
	}

	@Override
	public void onNext(CompanySearchResult companySearchResult) {
		showState = ShowState.SEARCH;
		getView().hideProgress();
		getView().showCompanySearchResult(companySearchResult);
		getView().changeFabImage(FabImage.FAB_IMAGE_SEARCH_CLOSE);
	}

	void search(String queryText) {
		this.queryText = queryText;
		getView().showProgress();
		dataManager.searchCompanies(queryText, "0")
				.subscribe(this);
	}

	void searchLoadMore(int page) {
		dataManager.searchCompanies(queryText, String.valueOf(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)))
				.subscribe(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	void getCompany(String companyName, String companyNumber) {
		getView().startCompanyActivity(companyNumber, companyName);
		searchHistoryItems = dataManager.addRecentSearchItem(new SearchHistoryItem(companyName, companyNumber, System.currentTimeMillis()));
	}

	void onFabClicked() {
		if(showState == ShowState.RECENT_SEARCHES) {
			showState = ShowState.SEARCH;
			getView().showDeleteRecentSearchesDialog();
		} else if(showState == ShowState.SEARCH) {
			getView().clearSearchView();
			showRecentSearches(false);
		}
	}

	void clearAllRecentSearches() {
		dataManager.clearAllRecentSearches();
		getView().refreshRecentSearchesAdapter(new ArrayList<>());
	}
}
