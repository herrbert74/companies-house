package com.babestudios.companyinfouk.ui.officerappointments

import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments

import net.grandcentrix.thirtyinch.TiView
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread

interface OfficerAppointmentsActivityView : TiView {

	@get:CallOnMainThread
	val officerId: String

	@CallOnMainThread
	fun showProgress()

	@CallOnMainThread
	fun hideProgress()

	@CallOnMainThread
	fun showError()

	@CallOnMainThread
	fun showAppointments(appointments: Appointments)

	fun startCompanyActivity(companyNumber: String, companyName: String)

}