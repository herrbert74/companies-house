package com.babestudios.companyinfouk.ui.officerappointments

import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments
import com.nhaarman.mockitokotlin2.any

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import io.reactivex.Observable

import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class OfficerAppointmentsPresenterTest {

	private lateinit var officerAppointmentsPresenter: OfficerAppointmentsPresenter

	@Before
	fun setUp() {
		officerAppointmentsPresenter = OfficerAppointmentsPresenter(mock(CompaniesRepository::class.java))
		val view = mock(OfficerAppointmentsActivityView::class.java)
		`when`(view.officerId).thenReturn("0")
		`when`(officerAppointmentsPresenter.companiesRepository.getOfficerAppointments("0", "0")).thenReturn(Observable.just(Appointments()))
		officerAppointmentsPresenter.create()
		officerAppointmentsPresenter.attachView(view)
	}

	@Test
	fun whenGetAppointments_thenDataManagerGetAppointmentsIsCalled() {
		officerAppointmentsPresenter.getAppointments()
		verify(officerAppointmentsPresenter.companiesRepository, times(2)).getOfficerAppointments("0", "0")
	}

	@Test
	fun whenOnNext_thenViewHideProgressAndShowAppointmentsIsCalled() {
		officerAppointmentsPresenter.onNext(Appointments())
		verify<OfficerAppointmentsActivityView>(officerAppointmentsPresenter.view, times(2)).hideProgress()
		verify<OfficerAppointmentsActivityView>(officerAppointmentsPresenter.view, times(2)).showAppointments(any())
	}
}
