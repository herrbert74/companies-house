package com.babestudios.companyinfouk.filings

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryLinks
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsExecutor
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsStore
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsStoreFactory
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test

class FilingHistoryDetailsTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var filingDetailsExecutor: FilingDetailsExecutor

	private lateinit var filingDetailsStore: FilingDetailsStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	private val documentResponseBody = "test".toResponseBody("text/plain".toMediaType())

	@Before
	fun setUp() {

		coEvery {
			companiesHouseRepository.getDocument(any())
		} answers {
			documentResponseBody
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

	@Test
	fun `when fetch document then document is downloaded`() {

		val states = filingDetailsStore.states.test()

		filingDetailsStore.accept(FilingDetailsStore.Intent.FetchDocument)

		states.last().downloadedPdfResponseBody shouldBe documentResponseBody

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
