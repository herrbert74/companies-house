package com.babestudios.companyinfouk.companies

import com.airbnb.mvrx.test.MvRxTestRule
import com.babestudios.base.ext.getPrivateFieldWithReflection
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.companies.ui.CompaniesState
import com.babestudios.companyinfouk.companies.ui.CompaniesViewModel
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.navigation.features.CompaniesNavigator
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class SearchTest {

	private val companiesHouseRepository = mockk<CompaniesRepositoryContract>()

	private val companiesNavigator = mockk<CompaniesNavigator>()

	private val errorResolver = mockk<ErrorResolver>()

	private val searchHistoryItem = SearchHistoryItem("TUI", "12344", 12L)

	@Before
	fun setUp() {
		every {
			companiesHouseRepository.addRecentSearchItem(searchHistoryItem)
		} answers
				{
					arrayListOf(searchHistoryItem)
				}
		every {
			companiesHouseRepository.clearAllRecentSearches()
		} answers
				{
					Exception("")
				}
		every {
			companiesNavigator.mainToCompany()
		} answers
				{
					Exception("")
				}
		every {
			companiesHouseRepository.searchCompanies(any(), any())
		} answers
				{
					Single.create { CompanySearchResult() }
				}

	}

	@Test
	fun whenFabClickedInStateRecentSearches_thenShowDeleteRecentSearchesDialogIsCalled() {
		val viewModel = companiesViewModel()
		viewModel.clearAllRecentSearches()
		val repo = viewModel.getPrivateFieldWithReflection<CompaniesRepositoryContract>("companiesRepository")
		verify(exactly = 1) { repo.clearAllRecentSearches() }
	}

	@Test
	fun `when searchItemClicked then addRecentSearchItem and StartActivity Is Called`() {
		val viewModel = companiesViewModel()
		viewModel.searchItemClicked("TUI", "12344")
		val repo = viewModel.getPrivateFieldWithReflection<CompaniesRepositoryContract>("companiesRepository")
		verify(exactly = 1) { repo.addRecentSearchItem(searchHistoryItem) }
		verify(exactly = 1) { viewModel.companiesNavigator.mainToCompany() }
	}

	@Test
	fun whenSearch_thenDataManagerSearchCompaniesIsCalled() {
		val viewModel = companiesViewModel()
		viewModel.search("")
		val repo = viewModel.getPrivateFieldWithReflection<CompaniesRepositoryContract>("companiesRepository")
		verify(exactly = 1) {repo.searchCompanies(any(), any())}
	}

	@Test
	fun whenSearchLoadMore_thenDataManagerSearchCompaniesIsCalled() {
		val viewModel = companiesViewModel()
		viewModel.loadMoreSearch(1)
		val repo = viewModel.getPrivateFieldWithReflection<CompaniesRepositoryContract>("companiesRepository")
		verify(exactly = 1) {repo.searchCompanies(any(), any())}
	}


	private fun companiesViewModel(): CompaniesViewModel {
		return CompaniesViewModel(
				CompaniesState(companyNumber = "123", totalCount = 50),
				companiesHouseRepository,
				companiesNavigator,
				errorResolver,
				recentSearchesString = "")
	}

	companion object {
		@JvmField
		@ClassRule
		val mvrxTestRule = MvRxTestRule()
	}
}
