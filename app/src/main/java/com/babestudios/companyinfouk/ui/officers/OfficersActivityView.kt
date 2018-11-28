package com.babestudios.companyinfouk.ui.officers

import com.babestudios.companyinfouk.data.model.officers.Officers

import net.grandcentrix.thirtyinch.TiView
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread

interface OfficersActivityView : TiView {

	@get:CallOnMainThread
	val companyNumber: String

	@CallOnMainThread
	fun showProgress()

	@CallOnMainThread
	fun hideProgress()

	@CallOnMainThread
	fun showError()

	@CallOnMainThread
	fun showOfficers(officers: Officers)

}