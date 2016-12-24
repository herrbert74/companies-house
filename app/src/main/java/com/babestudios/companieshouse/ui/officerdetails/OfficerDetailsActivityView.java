package com.babestudios.companieshouse.ui.officerdetails;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

interface OfficerDetailsActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();
}