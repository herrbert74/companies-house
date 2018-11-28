package com.babestudios.companyinfouk.ui.officerdetails

import net.grandcentrix.thirtyinch.TiView
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread

interface OfficerDetailsActivityView : TiView {

	@CallOnMainThread
	fun showProgress()

	@CallOnMainThread
	fun hideProgress()
}