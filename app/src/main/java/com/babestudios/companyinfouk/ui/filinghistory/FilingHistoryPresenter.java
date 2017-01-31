package com.babestudios.companyinfouk.ui.filinghistory;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.babestudios.companyinfouk.BuildConfig;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList;
import com.babestudios.companyinfouk.ui.search.SearchPresenter;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import rx.Observer;

public class FilingHistoryPresenter extends TiPresenter<FilingHistoryActivityView> implements Observer<FilingHistoryList> {

	enum CategoryFilter {
		CATEGORY_SHOW_ALL ("all"),
		CATEGORY_GAZETTE("gazette"),
		CATEGORY_CONFIRMATION_STATEMENT("confirmation-statement"),
		CATEGORY_ACCOUNTS("accounts"),
		CATEGORY_ANNUAL_RETURN("annual return"),
		CATEGORY_OFFICERS("officers"),
		CATEGORY_ADDRESS("address"),
		CATEGORY_CAPITAL("capital"),
		CATEGORY_INSOLVENCY("insolvency"),
		CATEGORY_OTHER("other"),
		CATEGORY_INCORPORATION("incorporation"),
		CATEGORY_MORTGAGE("mortgage");

		private final String name;

		CategoryFilter(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
	
	DataManager dataManager;

	private FilingHistoryList filingHistoryList;

	private CategoryFilter categoryFilter = CategoryFilter.CATEGORY_SHOW_ALL;

	@Inject
	public FilingHistoryPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
	}

	@Override
	protected void onAttachView(@NonNull FilingHistoryActivityView view) {
		super.onAttachView(view);
		if(filingHistoryList != null) {
			view.showFilingHistory(filingHistoryList, categoryFilter);
			view.setInitialCategoryFilter(categoryFilter);
		} else {
			view.showProgress();
			getFilingHistory(view.getCompanyNumber(), view.getFilingCategory());
		}
	}

	@VisibleForTesting
	public void getFilingHistory(String companyNumber, String category) {
		dataManager.getFilingHistory(companyNumber, category, "0").subscribe(this);
	}

	public void loadMoreFilingHistory(int page) {
		dataManager.getFilingHistory(getView().getCompanyNumber(), getView().getFilingCategory(), String.valueOf(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE))).subscribe(this);
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
	public void onNext(FilingHistoryList filingHistoryList) {
		this.filingHistoryList = filingHistoryList;
		getView().hideProgress();
		getView().showFilingHistory(filingHistoryList, categoryFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	void setCategoryFilter(int category) {
		categoryFilter = CategoryFilter.values()[category];
		if (getView() != null) {
			getView().setFilterOnAdapter(categoryFilter);
		}
	}

}
