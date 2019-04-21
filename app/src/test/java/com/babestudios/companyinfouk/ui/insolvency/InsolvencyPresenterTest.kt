package com.babestudios.companyinfouk.ui.insolvency

import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class InsolvencyPresenterTest {

	private var insolvencyPresenter: InsolvencyPresenter? = null

	@Before
	fun setUp() {
		insolvencyPresenter = InsolvencyPresenter(mock(CompaniesRepository::class.java))
		val view = mock(InsolvencyActivityView::class.java)
		`when`(view.companyNumber).thenReturn("0")
		`when`(insolvencyPresenter!!.companiesRepository.getInsolvency("0")).thenReturn(Single.just(Insolvency()))
		insolvencyPresenter!!.create()
		insolvencyPresenter!!.attachView(view)
	}

	@Test
	fun test_When_GetInsolvency_Then_DataManagerGetInsolvencyIsCalled() {
		insolvencyPresenter!!.getInsolvency()
		verify(insolvencyPresenter!!.companiesRepository, times(2)).getInsolvency("0")
	}
}
