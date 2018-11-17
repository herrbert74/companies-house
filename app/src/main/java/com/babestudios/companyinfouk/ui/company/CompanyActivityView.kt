package com.babestudios.companyinfouk.ui.company

import com.babestudios.companyinfouk.data.model.company.Company

import net.grandcentrix.thirtyinch.TiView
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread

interface CompanyActivityView : TiView {

	@get:CallOnMainThread
	val companyNumber: String

	@get:CallOnMainThread
	val companyName: String

	@CallOnMainThread
	fun showFab()

	@CallOnMainThread
	fun hideFab()

	@CallOnMainThread
	fun showError()

	@CallOnMainThread
	fun showProgress()

	@CallOnMainThread
	fun hideProgress()

	@CallOnMainThread
	fun showCompany(company: Company)

	@CallOnMainThread
	fun showNatureOfBusiness(sicCode: String, natureOfBusiness: String)

	fun showEmptyNatureOfBusiness()
}