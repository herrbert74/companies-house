package com.babestudios.companieshouse.ui.officerappointments;

import com.babestudios.companieshouse.data.model.officers.appointments.Appointments;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

interface OfficerAppointmentsActivityView extends TiView {
	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showAppointments(final Appointments appointments);

	@CallOnMainThread
	String getOfficerId();

	void showError();
}