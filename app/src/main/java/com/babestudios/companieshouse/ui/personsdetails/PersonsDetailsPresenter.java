package com.babestudios.companieshouse.ui.personsdetails;

import com.babestudios.companieshouse.data.DataManager;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

public class PersonsDetailsPresenter extends TiPresenter<PersonsDetailsActivityView> {


	DataManager dataManager;

	@Inject
	public PersonsDetailsPresenter(DataManager dataManager) {
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
