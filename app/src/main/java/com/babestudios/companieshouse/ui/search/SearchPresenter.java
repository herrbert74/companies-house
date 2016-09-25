package com.babestudios.companieshouse.ui.search;

import android.util.Base64;
import android.util.Log;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.CompanySearchResult;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;

public class SearchPresenter extends TiPresenter<SearchActivityView> implements Observer<CompanySearchResult> {

	@Inject
	DataManager dataManager;

	private Subscription subscription;


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
}
