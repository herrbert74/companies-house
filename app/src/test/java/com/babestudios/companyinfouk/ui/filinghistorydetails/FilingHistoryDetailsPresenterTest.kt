package com.babestudios.companyinfouk.ui.filinghistorydetails

import com.babestudios.companyinfouk.data.DataManager

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.ResponseBody

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class FilingHistoryDetailsPresenterTest {

	private var filingHistoryDetailsPresenter: FilingHistoryDetailsPresenter? = null

	@Before
	fun setUp() {
		filingHistoryDetailsPresenter = FilingHistoryDetailsPresenter(mock(DataManager::class.java))
		val view = mock(FilingHistoryDetailsActivityView::class.java)
		`when`(view.filingHistoryItemString).thenReturn("0")
		`when`(filingHistoryDetailsPresenter!!.dataManager.getDocument("0")).thenReturn(Observable.just(ResponseBody.create(MediaType.parse("text/plain"), "test")))
		//when(filingHistoryDetailsPresenter.dataManager.writeDocumentPdf(any(ResponseBody.class))).thenReturn(Uri.parse("http:\\some.com"));
		filingHistoryDetailsPresenter!!.create()
		filingHistoryDetailsPresenter!!.attachView(view)
	}

	@Test
	fun whenGetDocument_thenDataManagerGetDocumentIsCalled() {
		filingHistoryDetailsPresenter!!.getDocument(filingHistoryItemString)
		verify(filingHistoryDetailsPresenter!!.dataManager).getDocument("0")
	}

	/*@Test
	public void whenWritePdf_thenDataManagerWriteDocumentPdfIsCalled() {
		filingHistoryDetailsPresenter.writePdf(ResponseBody.create(MediaType.parse("text/plain"), "test"));
		verify(filingHistoryDetailsPresenter.dataManager).writeDocumentPdf(any(ResponseBody.class));
	}*/
}
