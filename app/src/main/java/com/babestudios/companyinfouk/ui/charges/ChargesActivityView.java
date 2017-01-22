package com.babestudios.companyinfouk.ui.charges;

import com.babestudios.companyinfouk.data.model.charges.Charges;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

interface ChargesActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showCharges(final Charges charges);

	@CallOnMainThread
	String getCompanyNumber();

	@CallOnMainThread
	void showNoCharges();

}