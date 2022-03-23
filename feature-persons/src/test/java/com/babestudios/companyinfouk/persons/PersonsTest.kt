package com.babestudios.companyinfouk.persons

import com.airbnb.mvrx.test.MvRxTestRule
import com.babestudios.base.ext.getPrivateProperty
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.domain.model.persons.PersonsResponse
import com.babestudios.companyinfouk.navigation.features.PersonsNavigator
import com.babestudios.companyinfouk.persons.ui.PersonsState
import com.babestudios.companyinfouk.persons.ui.PersonsViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class PersonsTest {

	private val companiesHouseRepository = mockk<CompaniesRxRepository>()

	private val personsNavigator = mockk<PersonsNavigator>()


	@Before
	fun setUp() {
		every {
			companiesHouseRepository.getPersons("123", "0")
		} answers
				{
					Single.create { PersonsResponse() }
				}
	}

	@Test
	fun whenGetPersons_thenRepoGetPersonsIsCalled() {
		val viewModel = personsViewModel()
		viewModel.fetchPersons("123")
		val repo: CompaniesRxRepository? = viewModel.getPrivateProperty("companiesRepository")
		verify(exactly = 1) { repo?.getPersons("123", "0") }
	}

	@Test
	fun whenLoadMorePersons_thenRepoLoadMorePersonsIsCalled() {
		val viewModel = personsViewModel()
		viewModel.loadMorePersons(0)
		val repo: CompaniesRxRepository? = viewModel.getPrivateProperty("companiesRepository")
		verify(exactly = 1) { repo?.getPersons("123", "0") }
	}

	private fun personsViewModel(): PersonsViewModel {
		return PersonsViewModel(
				PersonsState(companyNumber = "123", totalPersonCount = 100),
				companiesHouseRepository,
				personsNavigator)
	}

	companion object {
		@JvmField
		@ClassRule
		val mvrxTestRule = MvRxTestRule()
	}
}
