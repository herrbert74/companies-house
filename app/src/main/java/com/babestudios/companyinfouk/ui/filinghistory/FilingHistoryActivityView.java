package com.babestudios.companyinfouk.ui.filinghistory;

import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

interface FilingHistoryActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showFilingHistory(final FilingHistoryList filingHistoryList);

	@CallOnMainThread
	String getCompanyNumber();

	@CallOnMainThread
	String getFilingCategory();
}