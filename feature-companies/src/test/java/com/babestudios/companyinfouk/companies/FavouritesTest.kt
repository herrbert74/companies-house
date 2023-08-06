package com.babestudios.companyinfouk.companies

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesExecutor
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesItem
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStoreFactory
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import io.kotest.matchers.collections.shouldContain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FavouritesTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var favouritesExecutor: FavouritesExecutor
	private lateinit var favouritesStore: FavouritesStore

	private val searchHistoryItem = SearchHistoryItem("TUI", "12344", 12L)

	private val testCoroutineDispatcher = StandardTestDispatcher()

	@Before
	fun setUp() {
		every { companiesHouseRepository.removeFavourite(searchHistoryItem) } returns Unit
		coEvery { companiesHouseRepository.logScreenView(any()) } returns Unit
		coEvery { companiesHouseRepository.favourites() } returns listOf(searchHistoryItem)

		favouritesExecutor = FavouritesExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		favouritesStore = FavouritesStoreFactory(DefaultStoreFactory(), favouritesExecutor).create()
	}

	@Test
	fun `when remove favourite the repo removes favourite after delay`() = runTest(testCoroutineDispatcher) {
		val states = favouritesStore.states.test()
		advanceUntilIdle()

		favouritesStore.accept(FavouritesStore.Intent.InitPendingRemoval(FavouritesItem(searchHistoryItem)))
		states.last().favourites shouldContain FavouritesItem(searchHistoryItem, true)

		advanceUntilIdle()
		coVerify(exactly = 1) { companiesHouseRepository.removeFavourite(searchHistoryItem) }
	}

	@Test
	fun `when undo remove favourite the repo does not remove favourite`() = runTest(testCoroutineDispatcher) {
		val states = favouritesStore.states.test()
		advanceUntilIdle()

		favouritesStore.accept(FavouritesStore.Intent.InitPendingRemoval(FavouritesItem(searchHistoryItem)))
		states.last().favourites shouldContain FavouritesItem(searchHistoryItem, true)
		favouritesStore.accept(FavouritesStore.Intent.CancelPendingRemoval(FavouritesItem(searchHistoryItem, true)))
		states.last().favourites shouldContain FavouritesItem(searchHistoryItem, false)

		advanceUntilIdle()
		coVerify(exactly = 0) { companiesHouseRepository.removeFavourite(searchHistoryItem) }
	}

}
