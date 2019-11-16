package com.babestudios.companyinfouk.filings

import com.airbnb.mvrx.test.MvRxTestRule
import com.babestudios.base.ext.getPrivateFieldWithReflection
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.filings.ui.FilingsState
import com.babestudios.companyinfouk.filings.ui.FilingsViewModel
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.navigation.features.FilingsNavigator
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class FilingHistoryTest {

	private val companiesHouseRepository = mockk<CompaniesRepositoryContract>()

	private val filingsNavigator = mockk<FilingsNavigator>()

	private val errorResolver = mockk<ErrorResolver>()

	@Before
	fun setUp() {
		every {
			companiesHouseRepository.getFilingHistory("123", any(), any())
		} answers
				{
					Single.create { FilingHistory() }
				}
	}

	@Test
	fun whenGetFilings_thenRepoGetFilingsIsCalled() {
		val viewModel = filingsViewModel()
		viewModel.getFilingHistory()
		val repo = viewModel.getPrivateFieldWithReflection<CompaniesRepositoryContract>("companiesRepository")
		verify(exactly = 1) { repo.getFilingHistory("123", any(),"0") }
	}

	@Test
	fun whenLoadMoreFilings_thenRepoLoadMoreFilingsIsCalled() {
		val viewModel = filingsViewModel()
		viewModel.loadMoreFilingHistory(1)
		val repo = viewModel.getPrivateFieldWithReflection<CompaniesRepositoryContract>("companiesRepository")
		verify(exactly = 1) { repo.getFilingHistory("123", any(),"100") }
	}

	private fun filingsViewModel(): FilingsViewModel {
		return FilingsViewModel(
				FilingsState(companyNumber = "123", totalFilingsCount = 100),
				companiesHouseRepository,
				filingsNavigator,
				errorResolver)
	}

	companion object {
		@JvmField
		@ClassRule
		val mvrxTestRule = MvRxTestRule()
	}
}
