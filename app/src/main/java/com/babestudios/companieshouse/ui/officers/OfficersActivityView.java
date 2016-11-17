package com.babestudios.companieshouse.ui.officers;

import com.babestudios.companieshouse.data.model.officers.Officers;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

interface OfficersActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showOfficers(final Officers officers);

	@CallOnMainThread
	String getCompanyNumber();

}