package com.babestudios.companyinfouk.ui.persons

import com.babestudios.companyinfouk.data.model.persons.Persons

import net.grandcentrix.thirtyinch.TiView
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread

interface PersonsActivityView : TiView {

	@get:CallOnMainThread
	val companyNumber: String

	@CallOnMainThread
	fun showProgress()

	@CallOnMainThread
	fun hideProgress()

	@CallOnMainThread
	fun showError()

	@CallOnMainThread
	fun showPersons(persons: Persons)

	@CallOnMainThread
	fun showNoPersons()

}