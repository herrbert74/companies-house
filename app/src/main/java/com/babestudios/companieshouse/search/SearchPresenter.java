package com.babestudios.companieshouse.search;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.network.SearchCompaniesService;
import com.babestudios.companieshouse.search.pojos.CompanySearchResult;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchPresenter extends TiPresenter<SearchActivityView> implements Observer<CompanySearchResult>{

	@Inject
	SearchCompaniesService searchCompaniesService;

	private final String authorization =
			"Basic " + Base64.encodeToString(BuildConfig.COMPANIES_HOUSE_API_KEY.getBytes(), Base64.NO_WRAP);

	private String queryText;
	@Override
	protected void onWakeUp() {
		super.onWakeUp();
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);

		//getView().showText("This will be the favorites list later");
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
		getView().showCompanySearchResult(companySearchResult);
	}

	public void search(CharSequence queryText) {
		this.queryText = queryText.toString();
		searchCompaniesService.searchCompanies(authorization, queryText.toString(), BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, "0")
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(this);
	}

	public void searchLoadMore(int page, int totalItemsCount) {
		searchCompaniesService.searchCompanies(authorization, queryText, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, String.valueOf(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE )))
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(this);
	}
}
