package com.babestudios.companyinfouk.ui.filinghistory;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.babestudios.companyinfouk.BuildConfig;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList;

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
	}

	@Override
	protected void onAttachView(@NonNull FilingHistoryActivityView view) {
		super.onAttachView(view);
		view.showProgress();
		getFilingHistory(view.getCompanyNumber(), view.getFilingCategory());
	}

	@VisibleForTesting
	public void getFilingHistory(String companyNumber, String category) {
		dataManager.getFilingHistory(companyNumber, category, "0").subscribe(this);
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
