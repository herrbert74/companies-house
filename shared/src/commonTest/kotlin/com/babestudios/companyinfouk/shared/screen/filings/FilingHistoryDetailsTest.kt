package com.babestudios.companyinfouk.shared.screen.filings

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryLinks
import com.babestudios.companyinfouk.shared.screen.filingdetails.FilingDetailsExecutor
import com.babestudios.companyinfouk.shared.screen.filingdetails.FilingDetailsStore
import com.babestudios.companyinfouk.shared.screen.filingdetails.FilingHistoryDetailsStoreFactory
import com.babestudios.companyinfouk.shared.domain.api.CompaniesDocumentRepository
import dev.mokkery.answering.calls
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlin.test.BeforeTest
import kotlinx.coroutines.Dispatchers


class FilingHistoryDetailsTest {

	private val companiesHouseRepository = mock<CompaniesDocumentRepository>()

	private lateinit var filingDetailsExecutor: FilingDetailsExecutor

	private lateinit var filingDetailsStore: FilingDetailsStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

//	private val documentResponseBody = "test".toResponseBody("text/plain".toMediaType())

	@BeforeTest
	fun setUp() {

		everySuspend {
			companiesHouseRepository.getDocument(any())
		} calls {
			mock()//.documentResponseBody
		}

		filingDetailsExecutor = FilingDetailsExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		val historyLinks = FilingHistoryLinks(documentMetadata = "something")
		val historyItem = FilingHistoryItem(links = historyLinks)

		filingDetailsStore =
			FilingHistoryDetailsStoreFactory(DefaultStoreFactory(), filingDetailsExecutor).create(
				selectedFilingHistoryItem = historyItem
			)

	}

//	@Test
//	fun `when fetch document then document is downloaded`() {
//
//		val states = filingDetailsStore.states.test()
//
//		filingDetailsStore.accept(FilingDetailsStore.Intent.FetchDocument)
//
//		states.last().downloadedPdfResponseBody shouldBe documentResponseBody
//
//	}

	/**
	 * TODO This goes through the Activity due to permission requests. Rewrite as an Espresso test?
	 */
	/*@Test
	fun whenWritePdf_thenDataManagerWriteDocumentPdfIsCalled() {
		filingHistoryDetailsPresenter.writeDocument()
		verify(filingHistoryDetailsPresenter.companiesRepository).writeDocumentPdf(any())
	}*/

}
