package com.babestudios.companieshouse.ui.company;

import com.babestudios.companieshouse.data.model.company.Company;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

public interface CompanyActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showCompany(final Company company);

	@CallOnMainThread
	String getCompanyNumber();
}