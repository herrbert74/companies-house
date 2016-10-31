package com.babestudios.companieshouse.ui.company;

import android.util.Base64;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.company.Company;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observer;

public class CompanyPresenter extends TiPresenter<CompanyActivityView> {

	@Singleton
	@Inject
	DataManager dataManager;

	private Company company;

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

	private void getCompany(String companyNumber) {
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
						CompanyPresenter.this.company = company;
						company.accounts.lastAccounts.type = dataManager.accountTypeLookup(company.accounts.lastAccounts.type);
						getView().showCompany(company);
						if (company.sicCodes != null && company.sicCodes.size() > 0) {
							getView().showNatureOfBusiness(company.sicCodes.get(0), dataManager.sicLookup(company.sicCodes.get(0)));
						} else {
							getView().showEmptyNatureOfBusiness();
						}
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	void onFabClicked() {
		dataManager.addFavourite(new SearchHistoryItem(company.companyName, company.companyNumber, 0));
	}
}