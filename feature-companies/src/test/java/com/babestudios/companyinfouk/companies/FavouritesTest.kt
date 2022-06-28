package com.babestudios.companyinfouk.companies

import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.ext.test
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesExecutor
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStoreFactory
import com.babestudios.companyinfouk.companies.ui.favourites.SideEffect
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesListItem
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class FavouritesTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var favouritesExecutor: FavouritesExecutor
	private lateinit var favouritesStore: FavouritesStore

	private val searchHistoryItem = SearchHistoryItem("TUI", "12344", 12L)

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@Before
	fun setUp() {
		every {
			companiesHouseRepository.removeFavourite(searchHistoryItem)
		} answers
			{
				Exception("")
			}
		coEvery {
			companiesHouseRepository.logScreenView(any())
		} answers { }
		coEvery {
			companiesHouseRepository.favourites()
		} answers
			{
				listOf(searchHistoryItem)
			}

		favouritesExecutor = FavouritesExecutor(
			testCoroutineDispatcher,
			testCoroutineDispatcher,
			companiesHouseRepository
		)

		favouritesStore = FavouritesStoreFactory(DefaultStoreFactory(), favouritesExecutor).create()
	}

	@Test
	fun `when favourites item clicked then navigates to company`() {
		val labels = favouritesStore.labels.test()
		favouritesStore.accept(FavouritesStore.Intent.FavouritesItemClicked(FavouritesListItem(searchHistoryItem)))
		labels.last().shouldBeTypeOf<SideEffect.FavouritesItemClicked>()
	}

	@Test
	fun `when remove favourite the repo removes favourite`() {
		val states = favouritesStore.states.test()
		favouritesStore.accept(FavouritesStore.Intent.RemoveItem(FavouritesListItem(searchHistoryItem)))
		states.last().shouldBeTypeOf<FavouritesStore.State.Show>()
		coVerify(exactly = 1) {
			companiesHouseRepository.removeFavourite(
				searchHistoryItem
			)
		}
	}

}
