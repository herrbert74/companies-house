package com.babestudios.companyinfouk.ui.officerappointments;

import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments;

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

	void startCompanyActivity(String companyNumber, String companyName);
}