package com.babestudios.companieshouse.ui.favourites;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;
import javax.inject.Singleton;

public class FavouritesPresenter extends TiPresenter<FavouritesActivityView> {

	@Singleton
	@Inject
	DataManager dataManager;

	@Override
	protected void onCreate() {
		super.onCreate();
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
	}

	@Override
	protected void onWakeUp() {
		super.onWakeUp();
		getView().showFavourites(dataManager.getFavourites());
	}

	void getCompany(String companyNumber, String companyName) {
		getView().startCompanyActivity(companyNumber, companyName);
	}

	void removeFavourite(SearchHistoryItem favouriteToRemove) {
		dataManager.removeFavourite(favouriteToRemove);
	}

}
