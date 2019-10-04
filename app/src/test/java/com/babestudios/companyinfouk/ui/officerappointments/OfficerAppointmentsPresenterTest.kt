package com.babestudios.companyinfouk.ui.officerappointments

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.DaggerTestApplicationComponent
import com.babestudios.companyinfouk.TestApplicationModule
import com.babestudios.companyinfo.data.model.officers.appointments.Appointments
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.CompletableSource
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OfficerAppointmentsPresenterTest {

	private lateinit var officerAppointmentsPresenter: OfficerAppointmentsPresenter

	@Before
	fun setUp() {
		val testApplicationComponent = DaggerTestApplicationComponent.builder()
				.testApplicationModule(TestApplicationModule(CompaniesHouseApplication()))
				.build()
		officerAppointmentsPresenter = testApplicationComponent.officerAppointmentsPresenter()
		whenever(officerAppointmentsPresenter.companiesRepository.getOfficerAppointments("0", "0")).thenReturn(Single.just(Appointments()))
		val officerAppointmentsViewModel = OfficerAppointmentsViewModel()
		officerAppointmentsViewModel.state.value.officerId = "0"
		officerAppointmentsPresenter.setViewModel(officerAppointmentsViewModel, CompletableSource { })
	}

	@Test
	fun whenGetAppointments_thenDataManagerGetAppointmentsIsCalled() {
		officerAppointmentsPresenter.fetchAppointments("0")
		verify(officerAppointmentsPresenter.companiesRepository, times(2)).getOfficerAppointments("0", "0")
	}
}