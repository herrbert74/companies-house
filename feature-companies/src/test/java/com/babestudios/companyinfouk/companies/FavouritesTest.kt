package com.babestudios.companyinfouk.companies

import com.airbnb.mvrx.test.MvRxTestRule
import com.babestudios.base.ext.getPrivateProperty
import com.babestudios.companyinfouk.companies.ui.CompaniesState
import com.babestudios.companyinfouk.companies.ui.CompaniesViewModel
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesListItem
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesVisitable
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.navigation.features.CompaniesBaseNavigatable
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class FavouritesTest {

	private val companiesHouseRepository = mockk<CompaniesRxRepository>()

	private val companiesNavigator = mockk<CompaniesBaseNavigatable>()

	private val searchHistoryItem = SearchHistoryItem("TUI", "12344", 12L)

	private val favouriteItems = listOf(FavouritesVisitable(FavouritesListItem(searchHistoryItem, false)))

	@Before
	fun setUp() {
		every {
			companiesHouseRepository.removeFavourite(searchHistoryItem)
		} answers
				{
					Exception("")
				}
		every {
			companiesHouseRepository.favourites()
		} answers
				{
					Single.create { searchHistoryItem }
				}
		every {
			companiesNavigator.favouritesToCompany(any(), any())
		} answers
				{
					Exception("")
				}
	}

	@Test
	fun `when favourites item clicked then navigates to company`() {
		val viewModel = companiesViewModel()
		viewModel.favouritesItemClicked(0)
		verify(exactly = 1) { viewModel.companiesNavigator.favouritesToCompany(any(), any()) }
	}

	@Test
	fun whenRemoveFavourite_shouldCallDataManagerRemoveFavourite() {
		val viewModel = companiesViewModel()
		viewModel.removeFavourite(searchHistoryItem)
		val repo: CompaniesRxRepository? = viewModel.getPrivateProperty("companiesRepository")
		verify(exactly = 1) { repo?.removeFavourite(searchHistoryItem) }
	}

	private fun companiesViewModel(): CompaniesViewModel {
		return CompaniesViewModel(
				CompaniesState(companyNumber = "123", totalCount = 50, favouriteItems = favouriteItems),
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
