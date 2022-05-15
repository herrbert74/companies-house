package com.babestudios.companyinfouk.filings

import com.airbnb.mvrx.test.MvRxTestRule
import com.babestudios.base.ext.getPrivateProperty
import com.babestudios.companyinfouk.filings.ui.FilingsState
import com.babestudios.companyinfouk.filings.ui.FilingsViewModel
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.navigation.features.FilingsBaseNavigatable
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class FilingHistoryTest {

	private val companiesHouseRepository = mockk<CompaniesRxRepository>()

	private val filingsNavigator = mockk<FilingsBaseNavigatable>()

	@Before
	fun setUp() {
		every {
			companiesHouseRepository.getFilingHistory("123", any(), any())
		} answers
				{
					Single.create { FilingHistoryDto() }
				}
	}

	@Test
	fun whenGetFilings_thenRepoGetFilingsIsCalled() {
		val viewModel = filingsViewModel()
		viewModel.getFilingHistory()
		val repo: CompaniesRxRepository? = viewModel.getPrivateProperty("companiesRepository")
		verify(exactly = 1) { repo?.getFilingHistory("123", any(), "0") }
	}

	@Test
	fun whenLoadMoreFilings_thenRepoLoadMoreFilingsIsCalled() {
		val viewModel = filingsViewModel()
		viewModel.loadMoreFilingHistory(1)
		val repo: CompaniesRxRepository? = viewModel.getPrivateProperty("companiesRepository")
		verify(exactly = 1) { repo?.getFilingHistory("123", any(), "100") }
	}

	private fun filingsViewModel(): FilingsViewModel {
		return FilingsViewModel(
				FilingsState(companyNumber = "123", totalFilingsCount = 100),
				companiesHouseRepository,
				filingsNavigator)
	}

	companion object {
		@JvmField
		@ClassRule
		val mvrxTestRule = MvRxTestRule()
	}
}
