package com.babestudios.companyinfouk.ui.company;

import android.support.annotation.NonNull;
import android.util.Log;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.company.Company;
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;

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
	protected void onAttachView(@NonNull final CompanyActivityView view) {
		super.onAttachView(view);
		if(company != null){
			showCompany(company);
		} else {
			getCompany(getView().getCompanyNumber());
		}
	}

	void getCompany(String companyNumber) {
		dataManager.getCompany(companyNumber)
				.subscribe(new Observer<Company>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.d("test", "onError: " + e.fillInStackTrace());
						if(getView()!= null) {
							getView().showError();
						}
					}

					@Override
					public void onNext(Company company) {
						company.accounts.lastAccounts.type = dataManager.accountTypeLookup(company.accounts.lastAccounts.type);
						CompanyPresenter.this.company = company;
						showCompany(company);
					}
				});
	}

	private void showCompany(Company company) {
		getView().showCompany(company);
		if (company.sicCodes != null && company.sicCodes.size() > 0) {
			getView().showNatureOfBusiness(company.sicCodes.get(0), dataManager.sicLookup(company.sicCodes.get(0)));
		} else {
			getView().showEmptyNatureOfBusiness();
		}
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