package com.babestudios.companyinfouk.companies

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.ext.test
import com.babestudios.companyinfouk.companies.ui.company.CompanyExecutor
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore
import com.babestudios.companyinfouk.companies.ui.company.CompanyStoreFactory
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.company.Company
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class CompanyTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var companyExecutor: CompanyExecutor

	private lateinit var companyStore: CompanyStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	private val searchHistoryItem = SearchHistoryItem("TUI", "12344", 0L)

	@Before
	fun setUp() {
		coEvery {
			companiesHouseRepository.logScreenView(any())
		} answers { }
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
		coEvery {
			companiesHouseRepository.getCompany(any())
		} answers
			{
				Company(
					companyName = searchHistoryItem.companyName,
					companyNumber = searchHistoryItem.companyNumber
				)
			}

		companyExecutor = CompanyExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		whenIsFavourite(false)
		companyStore = CompanyStoreFactory(DefaultStoreFactory(), companyExecutor).create("12344")
	}

	//region tests

	@Test
	fun `test when fab clicked then repo add favourite is called`() {
		val states = companyStore.states.test()
		states.last().shouldBeTypeOf<CompanyStore.State.Show>()
		companyStore.accept(CompanyStore.Intent.FabFavouritesClicked)
		coVerify(exactly = 1) { companiesHouseRepository.addFavourite(searchHistoryItem) }
	}

	@Test
	fun `when is favourite and fab clicked then repo remove favourite is called`() {
		whenIsFavourite(true)
		companyStore.accept(CompanyStore.Intent.FabFavouritesClicked)
		coVerify(exactly = 1) { companiesHouseRepository.removeFavourite(searchHistoryItem) }
	}

	@Test
	fun `when get company then repo get company is called`() {
		val states = companyStore.states.test()
		states.last().shouldBeTypeOf<CompanyStore.State.Show>()
		coVerify(exactly = 1) { companiesHouseRepository.getCompany(searchHistoryItem.companyNumber) }
	}

	//endregion

	//region mocking

	private fun whenIsFavourite(isFavourite: Boolean) {
		every {
			companiesHouseRepository.isFavourite(searchHistoryItem)
		} answers
			{
				isFavourite
			}
	}

	//endregion

//	private fun companiesViewModel(): CompaniesViewModel {
//		return CompaniesViewModel(
//			CompaniesState(companyNumber = "12344", companyName = "TUI"),
//			companiesHouseRepository,
//			companiesNavigator,
//			recentSearchesString = ""
//		)
//	}

}
