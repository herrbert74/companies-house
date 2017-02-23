package com.babestudios.companyinfouk.ui.search;

import android.support.annotation.NonNull;
import android.util.Log;

import com.babestudios.companyinfouk.BuildConfig;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;

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

	enum FilterState {
		FILTER_SHOW_ALL("all"),
		FILTER_ACTIVE("active"),
		FILTER_LIQUIDATION("liquidation"),
		FILTER_OPEN("open"),
		FILTER_DISSOLVED("dissolved");

		private final String name;

		FilterState(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private ShowState showState = ShowState.RECENT_SEARCHES;

	DataManager dataManager;

	private String queryText;

	private ArrayList<SearchHistoryItem> searchHistoryItems = null;
	private CompanySearchResult companySearchResult;

	private FilterState filterState = FilterState.FILTER_SHOW_ALL;

	private SearchActivityView searchActivityView;

	@Inject
	public SearchPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onAttachView(@NonNull final SearchActivityView view) {
		super.onAttachView(view);
		searchActivityView = view;
		if (showState == ShowState.RECENT_SEARCHES) {
			showRecentSearches();
		} else {
			view.clearSearchView();
			if (companySearchResult != null) {
				view.showCompanySearchResult(companySearchResult, false, filterState);
				view.setInitialFilterState(filterState);
				view.changeFabImage(FabImage.FAB_IMAGE_SEARCH_CLOSE);
			}
		}
	}

	private void showRecentSearches() {
		searchHistoryItems = new ArrayList<>(dataManager.getRecentSearches());
		searchActivityView.showRecentSearches(searchHistoryItems);
		searchActivityView.changeFabImage(FabImage.FAB_IMAGE_RECENT_SEARCH_DELETE);
		showState = ShowState.RECENT_SEARCHES;
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		Log.d("test", "onError: " + e.fillInStackTrace());
		if (searchActivityView != null) {
			searchActivityView.showError();
			searchActivityView.hideProgress();
		}
	}

	@Override
	public void onNext(CompanySearchResult companySearchResult) {
		showState = ShowState.SEARCH;
		this.companySearchResult = companySearchResult;
		searchActivityView.hideProgress();
		searchActivityView.showCompanySearchResult(companySearchResult, true, filterState);
		searchActivityView.changeFabImage(FabImage.FAB_IMAGE_SEARCH_CLOSE);
	}

	void search(String queryText) {
		this.queryText = queryText;
		searchActivityView.showProgress();
		dataManager.searchCompanies(queryText, "0").subscribe(this);
	}

	void searchLoadMore(int page) {
		dataManager.searchCompanies(queryText, String.valueOf(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)))
				.subscribe(this);
	}

	void getCompany(String companyName, String companyNumber) {
		searchActivityView.startCompanyActivity(companyNumber, companyName);
		searchHistoryItems = dataManager.addRecentSearchItem(new SearchHistoryItem(companyName, companyNumber, System.currentTimeMillis()));
	}

	void onFabClicked() {
		if (showState == ShowState.RECENT_SEARCHES) {
			searchActivityView.showDeleteRecentSearchesDialog();
		} else if (showState == ShowState.SEARCH) {
			searchActivityView.clearSearchView();
			searchActivityView.refreshRecentSearchesAdapter(searchHistoryItems);
			showRecentSearches();
		}
	}

	void clearAllRecentSearches() {
		dataManager.clearAllRecentSearches();
		searchActivityView.refreshRecentSearchesAdapter(new ArrayList<>());
	}

	void setFilterState(int state) {
		filterState = FilterState.values()[state];
		if (searchActivityView != null && showState == ShowState.SEARCH) {
			searchActivityView.setFilterOnAdapter(filterState);
		}
	}
}
