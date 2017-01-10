package com.babestudios.companieshouse.ui.filinghistorydetails;

import android.net.Uri;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

interface FilingHistoryDetailsActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showDocument(Uri pdfBytes);

	String getFilingHistoryItemString();
}