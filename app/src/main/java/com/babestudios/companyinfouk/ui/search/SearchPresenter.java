package com.babestudios.companyinfouk.ui.search;

import android.support.annotation.NonNull;
import android.util.Log;

import com.babestudios.companyinfouk.BuildConfig;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;

import net.grandcentrix.thirtyinch.TiPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observer;

public class SearchPresenter extends TiPresenter<SearchActivityView> implements Observer<CompanySearchResult> {

	enum FabImage {
		FAB_IMAGE_RECENT_SEARCH_DELETE,
		FAB_IMAGE_SEARCH_CLOSE
	}

	private enum ShowState {
		RECENT_SEARCHES,
		SEARCH
	}

	private ShowState showState = ShowState.RECENT_SEARCHES;

	DataManager dataManager;

	private String queryText;

	CompanySearchResult companySearchResult;

	@Inject
	public SearchPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onAttachView(@NonNull final SearchActivityView view) {
		super.onAttachView(view);
		if (showState == ShowState.RECENT_SEARCHES) {
			showRecentSearches();
		} else {
			view.clearSearchView();
			if(companySearchResult != null){
				view.showCompanySearchResult(companySearchResult);
				view.changeFabImage(FabImage.FAB_IMAGE_SEARCH_CLOSE);
			}
		}
	}

	private void showRecentSearches() {
		getView().showRecentSearches(dataManager.getRecentSearches());
		getView().changeFabImage(FabImage.FAB_IMAGE_RECENT_SEARCH_DELETE);
		showState = ShowState.RECENT_SEARCHES;
	}

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
	public void onNext(CompanySearchResult companySearchResult) {
		showState = ShowState.SEARCH;
		this.companySearchResult = companySearchResult;
		getView().hideProgress();
		getView().showCompanySearchResult(companySearchResult);
		getView().changeFabImage(FabImage.FAB_IMAGE_SEARCH_CLOSE);
	}

	void search(String queryText) {
		this.queryText = queryText;
		getView().showProgress();
		dataManager.searchCompanies(queryText, "0").subscribe(this);
	}

	void searchLoadMore(int page) {
		dataManager.searchCompanies(queryText, String.valueOf(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)))
				.subscribe(this);
	}

	void getCompany(String companyName, String companyNumber) {
		getView().startCompanyActivity(companyNumber, companyName);
	}

	void onFabClicked() {
		if (showState == ShowState.RECENT_SEARCHES) {
			showState = ShowState.SEARCH;
			getView().showDeleteRecentSearchesDialog();
		} else if (showState == ShowState.SEARCH) {
			getView().clearSearchView();
			showRecentSearches();
		}
	}

	void clearAllRecentSearches() {
		dataManager.clearAllRecentSearches();
		getView().refreshRecentSearchesAdapter(new ArrayList<>());
	}
}
