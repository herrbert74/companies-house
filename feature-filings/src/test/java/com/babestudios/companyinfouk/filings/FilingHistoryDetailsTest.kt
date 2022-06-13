package com.babestudios.companyinfouk.filings

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.common.ext.test
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryLinks
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsExecutor
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsStore
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsStoreFactory
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test

class FilingHistoryDetailsTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var filingHistoryDetailsExecutor: FilingHistoryDetailsExecutor

	private lateinit var filingHistoryDetailsStore: FilingHistoryDetailsStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	private val documentResponseBody = "test".toResponseBody("text/plain".toMediaType())

	@Before
	fun setUp() {

		coEvery {
			companiesHouseRepository.getDocument("123")
		} answers {
			documentResponseBody
		}

		filingHistoryDetailsExecutor = FilingHistoryDetailsExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		val historyLinks = FilingHistoryLinks(documentMetadata = "something")
		val historyItem = FilingHistoryItem(links = historyLinks)

		filingHistoryDetailsStore =
			FilingHistoryDetailsStoreFactory(DefaultStoreFactory(), filingHistoryDetailsExecutor).create(
				companyNumber = "123", selectedFilingHistoryItem = historyItem
			)

	}

	@Test
	fun `when fetch document then document is downloaded`() {

		val states = filingHistoryDetailsStore.states.test()

		filingHistoryDetailsStore.accept(FilingHistoryDetailsStore.Intent.FetchDocument("123"))

		states.last().shouldBeTypeOf<FilingHistoryDetailsStore.State.DocumentDownloaded>()
		(states.last() as? FilingHistoryDetailsStore.State.DocumentDownloaded)?.responseBody shouldBe documentResponseBody

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
