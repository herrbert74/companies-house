package com.babestudios.companyinfouk.ui.officers

import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.officers.Officers
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OfficersPresenterTest {

	private var officersPresenter: OfficersPresenter? = null

	@Before
	fun setUp() {
		officersPresenter = OfficersPresenter(mock(CompaniesRepository::class.java))
		val view = mock(OfficersActivityView::class.java)
		`when`(view.companyNumber).thenReturn("0")
		`when`(officersPresenter!!.companiesRepository.getOfficers("0", "0")).thenReturn(Single.just(Officers()))
		officersPresenter!!.create()
		officersPresenter!!.attachView(view)
	}

	@Test
	fun whenGetInsolvency_thenDataManagerGetInsolvencyIsCalled() {
		officersPresenter!!.getOfficers()
		verify(officersPresenter!!.companiesRepository, times(2)).getOfficers("0", "0")
	}
}
