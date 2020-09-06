package com.babestudios.companyinfouk.companies

import com.airbnb.mvrx.test.MvRxTestRule
import com.babestudios.base.ext.getPrivateProperty
import com.babestudios.companyinfouk.companies.ui.CompaniesState
import com.babestudios.companyinfouk.companies.ui.CompaniesViewModel
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.navigation.features.CompaniesNavigator
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class CompanyTest {

	private val companiesHouseRepository = mockk<CompaniesRepositoryContract>()

	private val companiesNavigator = mockk<CompaniesNavigator>()

	private val searchHistoryItem = SearchHistoryItem("TUI", "12344", 0L)

	@Before
	fun setUp() {
		every {
			companiesHouseRepository.removeFavourite(searchHistoryItem)
		} answers
				{
					Exception("")
				}
		every {
			companiesHouseRepository.addFavourite(searchHistoryItem)
		} answers
				{
					true
				}
		every {
			companiesHouseRepository.getCompany(any())
		} answers
				{
					Single.create { CompanyDto(companyNumber = "123456") }
				}
	}

	@Test
	fun test_When_FabClicked_Then_DataManagerAddFavouriteIsCalled() {
		every {
			companiesHouseRepository.isFavourite(searchHistoryItem)
		} answers
				{
					false
				}
		val viewModel = companiesViewModel()
		viewModel.flipCompanyFavoriteState()
		val repo: CompaniesRepositoryContract? = viewModel.getPrivateProperty("companiesRepository")
		verify(exactly = 1) { repo?.addFavourite(searchHistoryItem) }
	}

	@Test
	fun test_When_FabClicked_Then_DataManagerRemoveFavouriteIsCalled() {
		every {
			companiesHouseRepository.isFavourite(searchHistoryItem)
		} answers
				{
					true
				}
		val viewModel = companiesViewModel()
		viewModel.flipCompanyFavoriteState()
		val repo: CompaniesRepositoryContract? = viewModel.getPrivateProperty("companiesRepository")
		verify(exactly = 1) { repo?.removeFavourite(searchHistoryItem) }
	}

	@Test
	fun test_When_GetCompany_Then_DataManagerGetCompanyIsCalled() {
		every {
			companiesHouseRepository.isFavourite(searchHistoryItem)
		} answers
				{
					true
				}
		val viewModel = companiesViewModel()
		viewModel.fetchCompany("123456")
		val repo: CompaniesRepositoryContract? = viewModel.getPrivateProperty("companiesRepository")
		verify(exactly = 1) { repo?.getCompany("123456") }
	}


	private fun companiesViewModel(): CompaniesViewModel {
		return CompaniesViewModel(
				CompaniesState(companyNumber = "12344", companyName = "TUI"),
				companiesHouseRepository,
				companiesNavigator,
				recentSearchesString = "")
	}

	companion object {
		@JvmField
		@ClassRule
		val mvrxTestRule = MvRxTestRule()
	}
}
