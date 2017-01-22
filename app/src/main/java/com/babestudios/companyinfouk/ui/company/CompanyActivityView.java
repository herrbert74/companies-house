package com.babestudios.companyinfouk.ui.company;

import com.babestudios.companyinfouk.data.model.company.Company;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

public interface CompanyActivityView extends TiView {

	@CallOnMainThread
	void showFab();

	@CallOnMainThread
	void hideFab();

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showCompany(final Company company);

	@CallOnMainThread
	void showNatureOfBusiness(String sicCode, final String natureOfBusiness);

	@CallOnMainThread
	String getCompanyNumber();

	void showEmptyNatureOfBusiness();
}