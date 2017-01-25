package com.babestudios.companyinfouk.ui.officers;

import com.babestudios.companyinfouk.data.model.officers.Officers;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

interface OfficersActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showError();

	@CallOnMainThread
	void showOfficers(final Officers officers);

	@CallOnMainThread
	String getCompanyNumber();

}