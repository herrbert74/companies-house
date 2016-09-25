package com.babestudios.companieshouse.data;

import android.os.AsyncTask;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.data.model.CompanySearchResult;
import com.babestudios.companieshouse.data.network.CompaniesHouseService;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class DataManager {

	CompaniesHouseService companiesHouseService;

	@Inject
	public DataManager(CompaniesHouseService companiesHouseService) {
		this.companiesHouseService = companiesHouseService;
	}

	public Observable<CompanySearchResult> searchCompanies(String authorization, CharSequence queryText, String startPage) {
		return companiesHouseService.searchCompanies(authorization, queryText.toString(), BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startPage);
				//.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				//.observeOn(AndroidSchedulers.mainThread());

	}
}
