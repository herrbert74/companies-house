package com.babestudios.companieshouse.ui.favourites;

import android.util.Base64;

import com.babestudios.companieshouse.BuildConfig;
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
		getView().showFavourites(dataManager.getFavourites());
	}

	void getCompany(String companyNumber) {
		getView().startCompanyActivity(companyNumber);
	}

	void removeFavourite(SearchHistoryItem favouriteToRemove) {
		dataManager.removeFavourite(favouriteToRemove);
	}

}
