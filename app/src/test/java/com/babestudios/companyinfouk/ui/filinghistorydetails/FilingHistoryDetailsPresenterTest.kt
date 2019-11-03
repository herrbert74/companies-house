package com.babestudios.companyinfouk.ui.filinghistorydetails

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.DaggerTestApplicationComponent
import com.babestudios.companyinfouk.TestApplicationModule
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryLinksDto
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.CompletableSource
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FilingHistoryDetailsPresenterTest {

	private lateinit var filingHistoryDetailsPresenter: FilingHistoryDetailsPresenter

	@Before
	fun setUp() {
		val testApplicationComponent = DaggerTestApplicationComponent.builder()
				.testApplicationModule(TestApplicationModule(CompaniesHouseApplication()))
				.build()
		filingHistoryDetailsPresenter = testApplicationComponent.filingHistoryDetailsPresenter()
		val historyLinks = FilingHistoryLinksDto()
		historyLinks.documentMetadata = "something"
		val filingHistoryItem = FilingHistoryItem()
		filingHistoryItem.links = historyLinks
		val filingHistoryDetailsViewModel = FilingHistoryDetailsViewModel()
		filingHistoryDetailsViewModel.state.value.filingHistoryItem = filingHistoryItem
		filingHistoryDetailsPresenter.setViewModel(filingHistoryDetailsViewModel, CompletableSource {  })
		whenever(filingHistoryDetailsPresenter.companiesRepository.getDocument("something")).thenReturn(Single.just(ResponseBody.create(MediaType.parse("text/plain"), "test")))
	}

	@Test
	fun whenGetDocument_thenDataManagerGetDocumentIsCalled() {
		filingHistoryDetailsPresenter.fetchDocument()
		verify(filingHistoryDetailsPresenter.companiesRepository)?.getDocument("something")
	}

	/**
	 * TODO This goes through the Activity due to permission requests. Rewrite as an Espresso test?
	 */
	/*@Test
	fun whenWritePdf_thenDataManagerWriteDocumentPdfIsCalled() {
		filingHistoryDetailsPresenter.writeDocument()
		verify(filingHistoryDetailsPresenter.companiesRepository).writeDocumentPdf(any())
	}*/
}
