package com.babestudios.companyinfouk.ui.officers

import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.officers.Officers

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
class OfficersPresenterTest {

	private var officersPresenter: OfficersPresenter? = null

	@Before
	fun setUp() {
		officersPresenter = OfficersPresenter(mock(CompaniesRepository::class.java))
		val view = mock(OfficersActivityView::class.java)
		`when`(view.companyNumber).thenReturn("0")
		`when`(officersPresenter!!.companiesRepository.getOfficers("0", "0")).thenReturn(Observable.just(Officers()))
		officersPresenter!!.create()
		officersPresenter!!.attachView(view)
	}

	@Test
	fun whenGetInsolvency_thenDataManagerGetInsolvencyIsCalled() {
		officersPresenter!!.getOfficers()
		verify(officersPresenter!!.companiesRepository, times(2)).getOfficers("0", "0")
	}
}
