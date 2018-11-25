package com.babestudios.companyinfouk.ui.insolvency

import com.babestudios.companyinfouk.data.DataManager
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency

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
class InsolvencyPresenterTest {

	private var insolvencyPresenter: InsolvencyPresenter? = null

	@Before
	fun setUp() {
		insolvencyPresenter = InsolvencyPresenter(mock(DataManager::class.java))
		val view = mock(InsolvencyActivityView::class.java)
		`when`(view.companyNumber).thenReturn("0")
		`when`(insolvencyPresenter!!.dataManager.getInsolvency("0")).thenReturn(Observable.just(Insolvency()))
		insolvencyPresenter!!.create()
		insolvencyPresenter!!.attachView(view)
	}

	@Test
	fun test_When_GetInsolvency_Then_DataManagerGetInsolvencyIsCalled() {
		insolvencyPresenter!!.getInsolvency()
		verify(insolvencyPresenter!!.dataManager, times(2)).getInsolvency("0")
	}
}
