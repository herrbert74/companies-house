package com.babestudios.companyinfouk.ui.filinghistorydetails

import android.net.Uri

import net.grandcentrix.thirtyinch.TiView
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread

import okhttp3.ResponseBody

interface FilingHistoryDetailsActivityView : TiView {

	val filingHistoryItemString: String

	@CallOnMainThread
	fun showProgress()

	@CallOnMainThread
	fun hideProgress()

	@CallOnMainThread
	fun showError()

	@CallOnMainThread
	fun showDocument(pdfBytes: Uri)

	fun checkPermissionAndWritePdf(responseBody: ResponseBody)
}