package com.babestudios.companyinfouk.ui.persons

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.DaggerTestApplicationComponent
import com.babestudios.companyinfouk.TestApplicationModule
import com.babestudios.companyinfouk.data.model.persons.Persons
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.CompletableSource
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PersonsPresenterTest {

	private lateinit var personsPresenter: PersonsPresenter

	@Before
	fun setUp() {
		val testApplicationComponent = DaggerTestApplicationComponent.builder()
				.testApplicationModule(TestApplicationModule(CompaniesHouseApplication()))
				.build()
		personsPresenter = testApplicationComponent.personsPresenter()
		whenever(personsPresenter.companiesRepository.getPersons("0", "0")).thenReturn(Single.just(Persons()))
		val personsViewModel = PersonsViewModel()
		personsViewModel.state.value.companyNumber = "0"
		personsPresenter.setViewModel(personsViewModel, CompletableSource { })
	}

	@Test
	fun test_When_GetInsolvency_Then_DataManagerGetInsolvencyIsCalled() {
		personsPresenter.fetchPersons("0")
		verify(personsPresenter.companiesRepository, times(2)).getPersons("0", "0")
	}
}
