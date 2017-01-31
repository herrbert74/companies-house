package com.babestudios.companyinfouk.ui.filinghistory;

import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList;
import com.babestudios.companyinfouk.ui.search.SearchPresenter;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

interface FilingHistoryActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showError();

	@CallOnMainThread
	void showFilingHistory(final FilingHistoryList filingHistoryList, FilingHistoryPresenter.CategoryFilter categoryFilter);

	@CallOnMainThread
	String getCompanyNumber();

	@CallOnMainThread
	String getFilingCategory();

	void setFilterOnAdapter(FilingHistoryPresenter.CategoryFilter filterState);

	void setInitialCategoryFilter(FilingHistoryPresenter.CategoryFilter filterState);
}