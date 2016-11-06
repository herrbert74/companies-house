package com.babestudios.companieshouse.data;

import android.os.AsyncTask;
import android.util.Base64;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.data.local.PreferencesHelper;
import com.babestudios.companieshouse.data.local.ApiLookupHelper;
import com.babestudios.companieshouse.data.model.company.Company;
import com.babestudios.companieshouse.data.model.search.CompanySearchResult;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;
import com.babestudios.companieshouse.data.network.CompaniesHouseService;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DataManager {

	private final String authorization =
			"Basic " + Base64.encodeToString(BuildConfig.COMPANIES_HOUSE_API_KEY.getBytes(), Base64.NO_WRAP);

	private CompaniesHouseService companiesHouseService;
	private PreferencesHelper preferencesHelper;
	private ApiLookupHelper apiLookupHelper = new ApiLookupHelper();

	public DataManager(CompaniesHouseService companiesHouseService, PreferencesHelper preferencesHelper) {
		this.companiesHouseService = companiesHouseService;
		this.preferencesHelper = preferencesHelper;
	}

	public String sicLookup(String code){
		return apiLookupHelper.sicLookup(code);
	}

	public String accountTypeLookup(String accountType){
		return apiLookupHelper.accountTypeLookup(accountType);
	}

	public Observable<CompanySearchResult> searchCompanies(CharSequence queryText, String startPage) {
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

	public Observable<Company> getCompany(String companyNumber) {
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
