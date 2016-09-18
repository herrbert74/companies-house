package com.babestudios.companieshouse.search;

import com.babestudios.companieshouse.search.pojos.CompanySearchResult;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

public interface SearchActivityView extends TiView {

	@CallOnMainThread
	void showCompanySearchResult(final CompanySearchResult companySearchResult);
}