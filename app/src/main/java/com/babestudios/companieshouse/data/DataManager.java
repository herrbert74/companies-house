package com.babestudios.companieshouse.data;

import android.os.AsyncTask;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.data.local.PreferencesHelper;
import com.babestudios.companieshouse.data.model.company.Company;
import com.babestudios.companieshouse.data.model.search.CompanySearchResult;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;
import com.babestudios.companieshouse.data.network.CompaniesHouseService;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class DataManager {

	private CompaniesHouseService companiesHouseService;
	private PreferencesHelper preferencesHelper;

	@Inject
	public DataManager(CompaniesHouseService companiesHouseService, PreferencesHelper preferencesHelper) {
		this.companiesHouseService = companiesHouseService;
		this.preferencesHelper = preferencesHelper;
	}

	public Observable<CompanySearchResult> searchCompanies(String authorization, CharSequence queryText, String startPage) {
		return companiesHouseService.searchCompanies(authorization, queryText.toString(), BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startPage)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread());
	}

	public ArrayList<SearchHistoryItem> addRecentSearchItem(SearchHistoryItem searchHistoryItem){
		return preferencesHelper.addRecentSearch(searchHistoryItem);
	}

	public SearchHistoryItem[] getRecentSearches(){
		return preferencesHelper.getRecentSearches();
	}

	public Observable<Company> getCompany(String authorization, String companyNumber) {
		return companiesHouseService.getCompany(authorization, companyNumber)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread());
	}

	public void clearAllRecentSearches() {
		preferencesHelper.clearAllRecentSearches();
	}

	public void addFavourite(SearchHistoryItem searchHistoryItem) {
		preferencesHelper.addFavourite(searchHistoryItem);
	}

	public SearchHistoryItem[] getFavourites(){
		return preferencesHelper.getFavourites();
	}

	public void removeFavourite(SearchHistoryItem favouriteToRemove) {
		preferencesHelper.removeFavourite(favouriteToRemove);
	}
}
