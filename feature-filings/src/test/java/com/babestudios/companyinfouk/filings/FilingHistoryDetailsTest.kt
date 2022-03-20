package com.babestudios.companyinfouk.filings

import com.airbnb.mvrx.test.MvRxTestRule
import com.babestudios.base.ext.getPrivateProperty
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryLinks
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.filings.ui.FilingsState
import com.babestudios.companyinfouk.filings.ui.FilingsViewModel
import com.babestudios.companyinfouk.navigation.features.FilingsNavigator
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class FilingHistoryDetailsTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private val filingsNavigator = mockk<FilingsNavigator>()

	@Before
	fun setUp() {
		every {
			companiesHouseRepository.getDocument("something")
		} answers {
			Single.just("test".toResponseBody("text/plain".toMediaType()))
		}
	}

	@Test
	fun whenGetDocument_thenDataManagerGetDocumentIsCalled() {
		val viewModel = filingsViewModel()
		viewModel.fetchDocument()
		val repo: CompaniesRepository? = viewModel.getPrivateProperty("companiesRepository")
		verify(exactly = 1) { repo?.getDocument("something") }
	}

	/**
	 * TODO This goes through the Activity due to permission requests. Rewrite as an Espresso test?
	 */
	/*@Test
	fun whenWritePdf_thenDataManagerWriteDocumentPdfIsCalled() {
		filingHistoryDetailsPresenter.writeDocument()
		verify(filingHistoryDetailsPresenter.companiesRepository).writeDocumentPdf(any())
	}*/

	private fun filingsViewModel(): FilingsViewModel {
		val historyLinks = FilingHistoryLinks().copy(documentMetadata = "something")
		val historyItem = FilingHistoryItem().copy(links = historyLinks)
		return FilingsViewModel(
				FilingsState(companyNumber = "123", totalFilingsCount = 100, filingHistoryItem = historyItem),
				companiesHouseRepository,
				filingsNavigator)
	}

	companion object {
		@JvmField
		@ClassRule
		val mvrxTestRule = MvRxTestRule()
	}
}
