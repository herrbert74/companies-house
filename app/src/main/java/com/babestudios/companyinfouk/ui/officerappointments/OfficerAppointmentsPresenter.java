package com.babestudios.companyinfouk.ui.officerappointments;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;

public class OfficerAppointmentsPresenter extends TiPresenter<OfficerAppointmentsActivityView> implements Observer<Appointments>{


	DataManager dataManager;

	@Inject
	public OfficerAppointmentsPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onCreate() {
		super.onCreate();


	}

	@Override
	protected void onAttachView(@NonNull OfficerAppointmentsActivityView view) {
		super.onAttachView(view);
		view.showProgress();
		getAppointments();
	}

	@VisibleForTesting
	public void getAppointments() {
		dataManager.getOfficerAppointments(getView().getOfficerId(), "0").subscribe(this);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	@Override
	public void onCompleted() {

	}

	@Override
	public void onError(Throwable e) {
		getView().hideProgress();
		Log.d("test", "onError: " + e.fillInStackTrace());
		if(e instanceof HttpException){
			HttpException h = (HttpException) e;
			if(h.code() == 404){
				getView().showError();
			}
		}
	}

	@Override
	public void onNext(Appointments appointments) {
		getView().hideProgress();
		getView().showAppointments(appointments);
	}
}
