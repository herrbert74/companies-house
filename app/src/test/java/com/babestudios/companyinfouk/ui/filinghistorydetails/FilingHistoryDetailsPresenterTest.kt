package com.babestudios.companyinfouk.ui.filinghistorydetails

import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryLinks
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FilingHistoryDetailsPresenterTest {

	private var filingHistoryDetailsPresenter: FilingHistoryDetailsPresenter? = null

	lateinit var view: FilingHistoryDetailsActivityView
	@Before
	fun setUp() {
		filingHistoryDetailsPresenter = FilingHistoryDetailsPresenter(mock(CompaniesRepository::class.java))
		view = mock(FilingHistoryDetailsActivityView::class.java)
		val historyLinks = FilingHistoryLinks()
		historyLinks.documentMetadata = "something"
		val filingHistoryItem = FilingHistoryItem()
		filingHistoryItem.links = historyLinks
		whenever(view.filingHistoryItemString).thenReturn(Gson().toJson(filingHistoryItem))
		whenever(filingHistoryDetailsPresenter?.companiesRepository?.getDocument("something")).thenReturn(Observable.just(ResponseBody.create(MediaType.parse("text/plain"), "test")))
		//whenever(filingHistoryDetailsPresenter.companiesRepository.writeDocumentPdf(any(ResponseBody.class))).thenReturn(Uri.parse("http:\\some.com"));
		filingHistoryDetailsPresenter?.create()
		filingHistoryDetailsPresenter?.attachView(view)
	}

	@Test
	fun whenGetDocument_thenDataManagerGetDocumentIsCalled() {
		filingHistoryDetailsPresenter?.getDocument(view.filingHistoryItemString)
		verify(filingHistoryDetailsPresenter?.companiesRepository)?.getDocument("something")
	}

	/*@Test
	public void whenWritePdf_thenDataManagerWriteDocumentPdfIsCalled() {
		filingHistoryDetailsPresenter.writePdf(ResponseBody.create(MediaType.parse("text/plain"), "test"));
		verify(filingHistoryDetailsPresenter.companiesRepository).writeDocumentPdf(any(ResponseBody.class));
	}*/
}
