package com.babestudios.companieshouse.ui.search;

import com.babestudios.companieshouse.data.model.CompanySearchResult;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

public interface SearchActivityView extends TiView {

	@CallOnMainThread
	void showCompanySearchResult(final CompanySearchResult companySearchResult);
}