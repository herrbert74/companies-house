package com.babestudios.companyinfouk.shared.screen.officers

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.officers.Appointment
import com.babestudios.companyinfouk.shared.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.shared.domain.model.officers.Officer
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsExecutor
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsStore
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsStoreFactory
import com.github.michaelbull.result.Ok
import dev.mokkery.answering.calls
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.Dispatchers

class AppointmentsTest {

	private val companiesHouseRepository = mock<CompaniesRepository>()

	private lateinit var appointmentsExecutor: AppointmentsExecutor

	private lateinit var appointmentsStore: AppointmentsStore

	private val appointment = Appointment()

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@BeforeTest
	fun setUp() {
		every {
			companiesHouseRepository.logScreenView(any())
		} calls { }

		everySuspend {
			companiesHouseRepository.getOfficerAppointments("1", any())
		} calls { Ok(AppointmentsResponse(name = "", totalResults = 5, items = listOf(appointment))) }

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
		verifySuspend(exactly(1)) { companiesHouseRepository.logScreenView("AppointmentsFragment") }
		verifySuspend(exactly(1)) { companiesHouseRepository.getOfficerAppointments("1", "0") }
	}

	@Test
	fun `when load more officer appointments then repo load more officers appointments is called`() {
		val states = appointmentsStore.states.test()
		appointmentsStore.accept(AppointmentsStore.Intent.LoadMoreAppointments)
		states.last().appointmentsResponse.items shouldBe listOf(appointment, appointment)
		verifySuspend(exactly(1)) { companiesHouseRepository.getOfficerAppointments("1", "0") }
	}


}
