package com.babestudios.companyinfouk.ui.persons

import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.persons.Persons

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
class PersonsPresenterTest {

	private lateinit var personsPresenter: PersonsPresenter

	@Before
	fun setUp() {
		personsPresenter = PersonsPresenter(mock(CompaniesRepository::class.java))
		val view = mock(PersonsActivityView::class.java)
		`when`(view.companyNumber).thenReturn("0")
		`when`(personsPresenter.companiesRepository.getPersons("0", "0")).thenReturn(Observable.just(Persons()))
		personsPresenter.create()
		personsPresenter.attachView(view)
	}

	@Test
	fun test_When_GetInsolvency_Then_DataManagerGetInsolvencyIsCalled() {
		personsPresenter.getPersons()
		verify(personsPresenter.companiesRepository, times(2)).getPersons("0", "0")
	}
}
