package com.babestudios.companyinfouk.shared.screen.companies

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.company.Company
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.shared.screen.company.CompanyExecutor
import com.babestudios.companyinfouk.shared.screen.company.CompanyStore
import com.babestudios.companyinfouk.shared.screen.company.CompanyStoreFactory
import dev.mokkery.answering.calls
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.Dispatchers

class CompanyTest {

	private val companiesHouseRepository = mock<CompaniesRepository>()

	private lateinit var companyExecutor: CompanyExecutor

	private lateinit var companyStore: CompanyStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	private val searchHistoryItem = SearchHistoryItem("TUI", "12344", 0L)

	@BeforeTest
	fun setUp() {
		everySuspend {
			companiesHouseRepository.logScreenView(any())
		} calls { }
		every {
			companiesHouseRepository.removeFavourite(searchHistoryItem)
		} calls
			{
				Exception("")
			}
		every {
			companiesHouseRepository.addFavourite(searchHistoryItem)
		} calls
			{
				true
			}
		everySuspend {
			companiesHouseRepository.getCompany(any())
		} calls
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
		states.last().isLoading shouldBe false
		companyStore.accept(CompanyStore.Intent.FabFavouritesClicked)
		verifySuspend(exactly(1)) { companiesHouseRepository.addFavourite(searchHistoryItem) }
	}

	@Test
	fun `when is favourite and fab clicked then repo remove favourite is called`() {
		whenIsFavourite(true)
		companyStore.accept(CompanyStore.Intent.FabFavouritesClicked)
		verifySuspend(exactly(1)) { companiesHouseRepository.removeFavourite(searchHistoryItem) }
	}

	@Test
	fun `when get company then repo get company is called`() {
		val states = companyStore.states.test()
		states.last().isLoading shouldBe false
		verifySuspend(exactly(1)) { companiesHouseRepository.getCompany(searchHistoryItem.companyNumber) }
	}

	//endregion

	//region mocking

	private fun whenIsFavourite(isFavourite: Boolean) {
		every {
			companiesHouseRepository.isFavourite(searchHistoryItem)
		} calls
			{
				isFavourite
			}
	}

	//endregion

}
