package com.babestudios.companieshouse.ui.officerdetails;

import net.grandcentrix.thirtyinch.TiPresenter;

public class OfficerDetailsPresenter extends TiPresenter<OfficerDetailsActivityView> {

	@Override
	protected void onCreate() {
		super.onCreate();
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
