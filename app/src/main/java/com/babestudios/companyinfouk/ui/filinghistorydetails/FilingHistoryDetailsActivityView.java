package com.babestudios.companyinfouk.ui.filinghistorydetails;

import android.net.Uri;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

import okhttp3.ResponseBody;

interface FilingHistoryDetailsActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showError();

	@CallOnMainThread
	void showDocument(Uri pdfBytes);

	String getFilingHistoryItemString();

	void checkPermissionAndWritePdf(ResponseBody responseBody);
}