package com.babestudios.companyinfouk.officers

import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.officers.ui.OfficersViewModel
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsExecutor
import com.babestudios.companyinfouk.officers.ui.officers.BootstrapIntent
import com.babestudios.companyinfouk.officers.ui.officers.OfficersExecutor
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStoreFactory
import com.github.michaelbull.result.Ok
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class OfficersTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private val testScheduler = TestCoroutineScheduler()
	private val dispatcher = StandardTestDispatcher(testScheduler)
	private val testScope = TestScope(dispatcher)

	private val officersExecutor = OfficersExecutor(companiesHouseRepository, dispatcher, dispatcher)

	@Before
	fun setUp() {
		isAssertOnMainThreadEnabled = false


		coEvery {
			companiesHouseRepository.getOfficers("123", "0")
		} answers
			{
				Ok(OfficersResponse())
			}

		coEvery {
			companiesHouseRepository.getOfficerAppointments("123", any())
		} answers { AppointmentsResponse(name = "", totalResults = 5) }

		every { companiesHouseRepository.logScreenView(any()) } returns Unit

		var officersStore: OfficersStore =
			OfficersStoreFactory(DefaultStoreFactory(), officersExecutor).create("123")
	}

	@Test
	fun whenGetOfficers_thenRepoGetOfficersIsCalled() = testScope.runTest {
		//officersExecutor.executeAction(BootstrapIntent.LoadOfficers("123"))
		officersExecutor.executeIntent(OfficersStore.Intent.LoadMoreOfficers(1) { OfficersStore.State.Show()})
		//val repo: CompaniesRxRepository? = viewModel.getPrivateProperty("companiesRepository")
		coVerify(exactly = 1) { companiesHouseRepository.logScreenView("OfficersFragment") }
		coVerify(exactly = 1) { companiesHouseRepository.getOfficers("123", "0") }
	}

//	@Test
//	fun whenLoadMoreOfficers_thenRepoLoadMoreOfficersIsCalled() {
//		val viewModel = officersViewModel()
//		viewModel.loadMoreOfficers(0)
//		val repo: CompaniesRxRepository? = viewModel.getPrivateProperty("companiesRepository")
//		coVerify(exactly = 1) { repo?.getOfficers("123", "0") }
//	}
//
//	@Test
//	fun whenGetOfficerAppointments_thenRepoGetOfficerAppointmentsIsCalled() {
//		val viewModel = officersViewModel()
//		viewModel.fetchAppointments()
//		val repo: CompaniesRxRepository? = viewModel.getPrivateProperty("companiesRepository")
//		verify(exactly = 1) { repo?.getOfficerAppointments("123", "0") }
//	}
//
//	@Test
//	fun whenLoadMoreOfficerAppointments_thenRepoLoadMoreOfficersAppointmentsIsCalled() {
//		val viewModel = officersViewModel()
//		viewModel.loadMoreAppointments(1)
//		val repo: CompaniesRxRepository? = viewModel.getPrivateProperty("companiesRepository")
//		verify(exactly = 1) { repo?.getOfficerAppointments("123", "100") }
//	}
//
//	private fun officersViewModel(): OfficersViewModel {
//		return OfficersViewModel(
//			OfficersState(
//				companyNumber = "123",
//				selectedOfficerId = "123",
//				totalOfficersCount = 100,
//				totalAppointmentsCount = 200
//			),
//			companiesHouseRepository,
//			officersNavigator
//		)
//	}
//
//	companion object {
//		@JvmField
//		@ClassRule
//		val mvrxTestRule = MvRxTestRule()
//	}
}
