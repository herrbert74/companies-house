package com.babestudios.companieshouse;

import android.os.AsyncTask;
import android.util.StringBuilderPrinter;

import com.babestudios.companieshouse.network.SearchCompaniesService;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchPresenter extends TiPresenter<SearchView> implements Observer<String>{

	@Inject
	SearchCompaniesService searchCompaniesService;

	@Override
	protected void onWakeUp() {
		super.onWakeUp();
		((CompaniesHouseApplication) CompaniesHouseApplication.getContext()).getApplicationComponent().inject(CompaniesHouseApplication.getInstance());
		searchCompaniesService.searchCompanies("alphabet", "1", "1")
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(this);
		getView().showText("This will be the favorites list later");
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onError(Throwable e) {

	}

	@Override
	public void onNext(String s) {
		String f = s;
	}
}
