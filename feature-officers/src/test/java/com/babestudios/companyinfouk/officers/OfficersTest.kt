package com.babestudios.companyinfouk.officers

import com.airbnb.mvrx.test.MvRxTestRule
import com.babestudios.base.ext.getPrivateFieldWithReflection
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.officers.Officers
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator
import com.babestudios.companyinfouk.officers.ui.OfficersState
import com.babestudios.companyinfouk.officers.ui.OfficersViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class OfficersTest {

	private val companiesHouseRepository = mockk<CompaniesRepositoryContract>()

	private val officersNavigator = mockk<OfficersNavigator>()

	private val errorResolver = mockk<ErrorResolver>()


	@Before
	fun setUp() {
		every {
			companiesHouseRepository.getOfficers("123", "0")
		} answers
				{
					Single.create { Officers() }
				}

		every {
			companiesHouseRepository.getOfficerAppointments("123", any())
		} answers
				{
					Single.create { Appointments() }
				}
	}

	@Test
	fun whenGetOfficers_thenRepoGetOfficersIsCalled() {
		val viewModel = officersViewModel()
		viewModel.fetchOfficers("123")
		val repo = viewModel.getPrivateFieldWithReflection<CompaniesRepositoryContract>("companiesRepository")
		verify(exactly = 1) { repo.getOfficers("123", "0") }
	}

	@Test
	fun whenLoadMoreOfficers_thenRepoLoadMoreOfficersIsCalled() {
		val viewModel = officersViewModel()
		viewModel.loadMoreOfficers(0)
		val repo = viewModel.getPrivateFieldWithReflection<CompaniesRepositoryContract>("companiesRepository")
		verify(exactly = 1) { repo.getOfficers("123", "0") }
	}

	@Test
	fun whenGetOfficerAppointments_thenRepoGetOfficerAppointmentsIsCalled() {
		val viewModel = officersViewModel()
		viewModel.fetchAppointments()
		val repo = viewModel.getPrivateFieldWithReflection<CompaniesRepositoryContract>("companiesRepository")
		verify(exactly = 1) { repo.getOfficerAppointments("123", "0") }
	}

	@Test
	fun whenLoadMoreOfficerAppointments_thenRepoLoadMoreOfficersAppointmentsIsCalled() {
		val viewModel = officersViewModel()
		viewModel.loadMoreAppointments(1)
		val repo = viewModel.getPrivateFieldWithReflection<CompaniesRepositoryContract>("companiesRepository")
		verify(exactly = 1) { repo.getOfficerAppointments("123", "100") }
	}

	private fun officersViewModel(): OfficersViewModel {
		return OfficersViewModel(
				OfficersState(
						companyNumber = "123",
						officerId = "123",
						totalOfficersCount = 100,
						totalAppointmentsCount = 200
				),
				companiesHouseRepository,
				officersNavigator,
				errorResolver)
	}

	companion object {
		@JvmField
		@ClassRule
		val mvrxTestRule = MvRxTestRule()
	}
}
