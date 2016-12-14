package com.babestudios.companieshouse.ui.filinghistorydetails;

import com.babestudios.companieshouse.data.DataManager;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

public class FilingHistoryDetailsPresenter extends TiPresenter<FilingHistoryDetailsActivityView> {


	DataManager dataManager;

	@Inject
	public FilingHistoryDetailsPresenter(DataManager dataManager) {
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
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
