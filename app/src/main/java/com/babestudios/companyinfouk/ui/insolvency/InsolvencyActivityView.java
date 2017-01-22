package com.babestudios.companyinfouk.ui.insolvency;

import com.babestudios.companyinfouk.data.model.insolvency.Insolvency;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

interface InsolvencyActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showInsolvency(final Insolvency insolvency);

	@CallOnMainThread
	String getCompanyNumber();

	@CallOnMainThread
	void showNoInsolvency();

}