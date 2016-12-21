package com.babestudios.companieshouse.ui.chargesdetails;

import com.babestudios.companieshouse.data.DataManager;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

public class ChargesDetailsPresenter extends TiPresenter<ChargesDetailsActivityView> {


	DataManager dataManager;

	@Inject
	public ChargesDetailsPresenter(DataManager dataManager) {
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
