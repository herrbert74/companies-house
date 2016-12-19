package com.babestudios.companieshouse.ui.insolvencydetails;

import com.babestudios.companieshouse.data.DataManager;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

public class InsolvencyDetailsPresenter extends TiPresenter<InsolvencyDetailsActivityView> {


	DataManager dataManager;

	@Inject
	public InsolvencyDetailsPresenter(DataManager dataManager) {
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
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
