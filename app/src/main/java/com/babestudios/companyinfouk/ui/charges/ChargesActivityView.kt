package com.babestudios.companyinfouk.ui.charges

import com.babestudios.companyinfouk.data.model.charges.Charges

import net.grandcentrix.thirtyinch.TiView
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread

interface ChargesActivityView : TiView {

	@get:CallOnMainThread
	val companyNumber: String

	@CallOnMainThread
	fun showProgress()

	@CallOnMainThread
	fun hideProgress()

	@CallOnMainThread
	fun showCharges(charges: Charges)

	@CallOnMainThread
	fun showNoCharges()

	fun showError()
}