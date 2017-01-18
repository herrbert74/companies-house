package com.babestudios.companieshouse.ui.favourites;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

public class FavouritesPresenter extends TiPresenter<FavouritesActivityView> {

	DataManager dataManager;

	@Inject
	public FavouritesPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
	}

	void onResume(){
		getView().showFavourites(dataManager.getFavourites());
	}

	void getCompany(String companyNumber, String companyName) {
		getView().startCompanyActivity(companyNumber, companyName);
	}

	void removeFavourite(SearchHistoryItem favouriteToRemove) {
		dataManager.removeFavourite(favouriteToRemove);
	}

}
