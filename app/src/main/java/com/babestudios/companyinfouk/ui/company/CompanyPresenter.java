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

	private Company company;
	/**
	 * It's safe to retrieve these from the view, because they come from the previous Activity, but not safe to get them from the company field, which might be null.
	 */
	private String companyNumber;
	private String companyName;

	private CompanyActivityView companyActivityView;

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
		companyActivityView = view;
		companyName = view.getCompanyName();
		companyNumber = view.getCompanyNumber();
		if (company != null) {
			showCompany(company);
		} else {
			getCompany(companyNumber);
		}
	}

	void getCompany(String companyNumber) {
		companyActivityView.showProgress();
		dataManager.getCompany(companyNumber)
				.subscribe(new Observer<Company>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.d("test", "onError: " + e.fillInStackTrace());
						companyActivityView.showError();
						companyActivityView.hideProgress();
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
		companyActivityView.showCompany(company);
		companyActivityView.hideProgress();
		if (company.sicCodes != null && company.sicCodes.size() > 0) {
			companyActivityView.showNatureOfBusiness(company.sicCodes.get(0), dataManager.sicLookup(company.sicCodes.get(0)));
		} else {
			companyActivityView.showEmptyNatureOfBusiness();
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

			if (dataManager.isFavourite(new SearchHistoryItem(companyName, companyNumber, 0))) {
				dataManager.removeFavourite(new SearchHistoryItem(companyName, companyNumber, 0));
			} else {
				dataManager.addFavourite(new SearchHistoryItem(companyName, companyNumber, 0));
			}
			companyActivityView.hideFab();
		});
	}
}