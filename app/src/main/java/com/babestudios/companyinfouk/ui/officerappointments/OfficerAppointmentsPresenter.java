package com.babestudios.companyinfouk.ui.officerappointments;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import rx.Observer;

public class OfficerAppointmentsPresenter extends TiPresenter<OfficerAppointmentsActivityView> implements Observer<Appointments>{


	DataManager dataManager;

	Appointments appointments;

	@Inject
	public OfficerAppointmentsPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onAttachView(@NonNull OfficerAppointmentsActivityView view) {
		super.onAttachView(view);
		if(appointments != null) {
			view.showAppointments(appointments);
		} else {
			view.showProgress();
			getAppointments();
		}
	}

	@VisibleForTesting
	public void getAppointments() {
		dataManager.getOfficerAppointments(getView().getOfficerId(), "0").subscribe(this);
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onError(Throwable e) {
		Log.d("test", "onError: " + e.fillInStackTrace());
		if(getView()!= null) {
			getView().hideProgress();
			getView().showError();
		}
	}

	@Override
	public void onNext(Appointments appointments) {
		this.appointments = appointments;
		getView().hideProgress();
		getView().showAppointments(appointments);
	}
}
