package com.babestudios.companieshouse.ui.officerdetails;

import com.babestudios.companieshouse.data.DataManager;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

public class OfficerDetailsPresenter extends TiPresenter<OfficerDetailsActivityView> {


	DataManager dataManager;

	@Inject
	public OfficerDetailsPresenter(DataManager dataManager) {
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
