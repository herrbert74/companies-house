package com.babestudios.companyinfouk.ui.insolvency

import com.babestudios.companyinfouk.data.model.insolvency.Insolvency

import net.grandcentrix.thirtyinch.TiView
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread

interface InsolvencyActivityView : TiView {

	@get:CallOnMainThread
	val companyNumber: String

	@CallOnMainThread
	fun showProgress()

	@CallOnMainThread
	fun hideProgress()

	@CallOnMainThread
	fun showError()

	@CallOnMainThread
	fun showInsolvency(insolvency: Insolvency)

	@CallOnMainThread
	fun showNoInsolvency()

}