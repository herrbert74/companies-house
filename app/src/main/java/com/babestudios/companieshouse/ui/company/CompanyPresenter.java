package com.babestudios.companieshouse.ui.company;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.company.Company;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;

public class CompanyPresenter extends TiPresenter<CompanyActivityView> {

	DataManager dataManager;

	Company company;

	@Inject
	public CompanyPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
	}

	@Override
	protected void onWakeUp() {
		super.onWakeUp();
		getCompany(getView().getCompanyNumber());
	}

	void getCompany(String companyNumber) {
		dataManager.getCompany(companyNumber)
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

	boolean isFavourite(SearchHistoryItem searchHistoryItem) {
		return dataManager.isFavourite(searchHistoryItem);
	}

	void observablesFromViews(Observable<Void> o) {
		o.subscribe(aVoid -> {
				if (dataManager.isFavourite(new SearchHistoryItem(company.companyName, company.companyNumber, 0))) {
					dataManager.removeFavourite(new SearchHistoryItem(company.companyName, company.companyNumber, 0));
				} else {
					dataManager.addFavourite(new SearchHistoryItem(company.companyName, company.companyNumber, 0));
				}
				getView().hideFab();
			});
	}
}