package com.babestudios.companieshouse.ui.search;

import android.util.Base64;
import android.util.Log;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.search.CompanySearchResult;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observer;

public class SearchPresenter extends TiPresenter<SearchActivityView> implements Observer<CompanySearchResult> {

	@Singleton
	@Inject
	DataManager dataManager;

	private final String authorization =
			"Basic " + Base64.encodeToString(BuildConfig.COMPANIES_HOUSE_API_KEY.getBytes(), Base64.NO_WRAP);

	private String queryText;

	private boolean isFirstStart = true;

	@Override
	protected void onCreate() {
		super.onCreate();
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);

	}

	@Override
	protected void onWakeUp() {
		super.onWakeUp();
		if(isFirstStart){
			isFirstStart = false;
			getView().showRecentSearches(dataManager.getRecentSearches());
		} else {
			getView().clearSearchView();
		}
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
		getView().hideProgress();
		getView().showCompanySearchResult(companySearchResult);
	}

	public void search(String queryText) {
		this.queryText = queryText;
		getView().showProgress();
		dataManager.searchCompanies(authorization, queryText, "0")
				.subscribe(this);
	}

	void searchLoadMore(int page) {
		dataManager.searchCompanies(authorization, queryText, String.valueOf(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)))
				.subscribe(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	void getCompany(String companyName, String companyNumber) {
		getView().startCompanyActivity(companyNumber);
		getView().refreshRecentSearchesAdapter(dataManager.putLatestSearchItem(new SearchHistoryItem(companyName, companyNumber, System.currentTimeMillis())));
	}
}
