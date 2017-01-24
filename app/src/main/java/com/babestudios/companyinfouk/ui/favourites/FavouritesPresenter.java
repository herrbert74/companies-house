package com.babestudios.companyinfouk.ui.favourites;

import android.support.annotation.NonNull;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;

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

	@Override
	public void onAttachView(@NonNull final FavouritesActivityView view){
		super.onAttachView(view);
		view.showFavourites(dataManager.getFavourites());
	}

	void getCompany(String companyNumber, String companyName) {
		getView().startCompanyActivity(companyNumber, companyName);
	}

	void removeFavourite(SearchHistoryItem favouriteToRemove) {
		dataManager.removeFavourite(favouriteToRemove);
	}

}
