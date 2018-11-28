package com.babestudios.companyinfouk.ui.officerappointments

import android.support.annotation.VisibleForTesting
import android.util.Log

import com.babestudios.companyinfouk.data.DataManager
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments

import net.grandcentrix.thirtyinch.TiPresenter

import javax.inject.Inject

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class OfficerAppointmentsPresenter @Inject
constructor(internal var dataManager: DataManager) : TiPresenter<OfficerAppointmentsActivityView>(), Observer<Appointments> {

	internal var appointments: Appointments? = null

	override fun onAttachView(view: OfficerAppointmentsActivityView) {
		super.onAttachView(view)
		appointments?.also {
			view.showAppointments(it)
		} ?: run {
			view.showProgress()
			getAppointments()
		}
	}

	@VisibleForTesting
	fun getAppointments() {
		view?.let {
			dataManager.getOfficerAppointments(it.officerId, "0").subscribe(this)
		}
	}

	override fun onComplete() {

	}

	override fun onSubscribe(d: Disposable) {

	}

	override fun onError(e: Throwable) {
		Log.d("test", "onError: " + e.fillInStackTrace())
		view?.let {
			it.hideProgress()
			it.showError()
		}
	}

	override fun onNext(appointments: Appointments) {
		this.appointments = appointments
		view?.let {
			it.hideProgress()
			it.showAppointments(appointments)
		}
	}
}
