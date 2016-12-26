package com.babestudios.companieshouse.ui.personsdetails;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

interface PersonsDetailsActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();
}