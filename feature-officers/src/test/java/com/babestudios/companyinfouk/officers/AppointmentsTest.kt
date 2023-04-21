package com.babestudios.companyinfouk.officers

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.officers.Appointment
import com.babestudios.companyinfouk.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsExecutor
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStoreFactory
import com.github.michaelbull.result.Ok
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class AppointmentsTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var appointmentsExecutor: AppointmentsExecutor

	private lateinit var appointmentsStore: AppointmentsStore

	private val appointment = Appointment()

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@Before
	fun setUp() {
		coEvery {
			companiesHouseRepository.logScreenView(any())
		} answers { }

		coEvery {
			companiesHouseRepository.getOfficerAppointments("1", any())
		} answers { Ok(AppointmentsResponse(name = "", totalResults = 5, items = listOf(appointment))) }

		appointmentsExecutor = AppointmentsExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		appointmentsStore = AppointmentsStoreFactory(DefaultStoreFactory(), appointmentsExecutor).create(
			Officer(appointmentsId = "1", fromToString = "From 2002-07-02 to 2002-07-02")
		)
	}

	@Test
	fun `when get officer appointments then repo get officer appointments is called`() {
		val states = appointmentsStore.states.test()
		states.last().appointmentsResponse.items shouldBe listOf(appointment)
		coVerify(exactly = 1) { companiesHouseRepository.logScreenView("AppointmentsFragment") }
		coVerify(exactly = 1) { companiesHouseRepository.getOfficerAppointments("1", "0") }
	}

	@Test
	fun `when load more officer appointments then repo load more officers appointments is called`() {
		val states = appointmentsStore.states.test()
		appointmentsStore.accept(AppointmentsStore.Intent.LoadMoreAppointments)
		states.last().appointmentsResponse.items shouldBe listOf(appointment, appointment)
		coVerify(exactly = 1) { companiesHouseRepository.getOfficerAppointments("1", "0") }
	}


}
