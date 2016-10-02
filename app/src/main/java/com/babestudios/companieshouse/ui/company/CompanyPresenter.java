package com.babestudios.companieshouse.ui.company;

import android.util.Base64;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.company.Company;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observer;

public class CompanyPresenter extends TiPresenter<CompanyActivityView> {

	@Singleton
	@Inject
	DataManager dataManager;

	private final String authorization =
			"Basic " + Base64.encodeToString(BuildConfig.COMPANIES_HOUSE_API_KEY.getBytes(), Base64.NO_WRAP);

	@Override
	protected void onCreate() {
		super.onCreate();
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);

	}

	@Override
	protected void onWakeUp() {
		super.onWakeUp();
		getCompany(getView().getCompanyNumber());
	}

	public void getCompany(String companyNumber) {
		dataManager.getCompany(authorization, companyNumber)
				.subscribe(new Observer<Company>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Company company) {
						getView().showCompany(company);
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}