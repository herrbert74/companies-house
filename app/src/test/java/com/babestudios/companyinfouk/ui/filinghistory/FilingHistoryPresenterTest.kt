package com.babestudios.companyinfouk.ui.filinghistory

import com.babestudios.companyinfouk.data.DataManager
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList

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
class FilingHistoryPresenterTest {

	private lateinit var filingHistoryPresenter: FilingHistoryPresenter

	@Before
	fun setUp() {
		filingHistoryPresenter = FilingHistoryPresenter(mock(DataManager::class.java))
		val view = mock(FilingHistoryActivityView::class.java)
		`when`(view.companyNumber).thenReturn("23")
		`when`<String>(view.filingCategory).thenReturn("0")
		`when`(filingHistoryPresenter.dataManager.getFilingHistory("23", "0", "0")).thenReturn(Observable.just(FilingHistoryList()))
		filingHistoryPresenter.create()
		filingHistoryPresenter.attachView(view)
	}

	@Test
	fun whenGetFilingHistory_thenDataManagerGetFilingHistoryIsCalled() {
		filingHistoryPresenter.getFilingHistory("23", "0")
		verify(filingHistoryPresenter.dataManager, times(2)).getFilingHistory("23", "0", "0")
	}
}
