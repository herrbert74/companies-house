package com.babestudios.companieshouse.ui.filinghistory;

import android.util.Log;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.filinghistory.FilingHistoryList;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import rx.Observer;

public class FilingHistoryPresenter extends TiPresenter<FilingHistoryActivityView> implements Observer<FilingHistoryList> {

	DataManager dataManager;

	@Inject
	public FilingHistoryPresenter(DataManager dataManager) {
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
		getView().showProgress();
		getFilingHistory();
	}

	private void getFilingHistory() {
		dataManager.getFilingHistory(getView().getCompanyNumber(), getView().getFilingCategory(), "0").subscribe(this);
	}

	public void loadMoreFilingHistory(int page) {
		dataManager.getFilingHistory(getView().getCompanyNumber(), getView().getFilingCategory(), String.valueOf(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE))).subscribe(this);
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		Log.d("test", "onError: " + e.fillInStackTrace());
	}

	@Override
	public void onNext(FilingHistoryList filingHistoryList) {
		getView().hideProgress();
		getView().showFilingHistory(filingHistoryList);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
