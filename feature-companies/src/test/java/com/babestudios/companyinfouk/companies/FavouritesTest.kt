package com.babestudios.companyinfouk.companies

import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesExecutor
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesItem
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStoreFactory
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.lighthousegames.logging.KmLogging

class FavouritesTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var favouritesExecutor: FavouritesExecutor
	private lateinit var favouritesStore: FavouritesStore

	private val searchHistoryItemTui = SearchHistoryItem("TUI", "12344", 12L)
	private val searchHistoryItemReach = SearchHistoryItem("Reach", "12345", 12L)
	private val searchHistoryItemAlma = SearchHistoryItem("Alma", "12346", 12L)
	private val searchHistoryItems = listOf(searchHistoryItemTui, searchHistoryItemReach, searchHistoryItemAlma)

	private val testCoroutineDispatcher = StandardTestDispatcher()

	@Before
	fun setUp() {
		KmLogging.setLoggers()
		every { companiesHouseRepository.removeFavourite(searchHistoryItemTui) } returns Unit
		every { companiesHouseRepository.removeFavourite(searchHistoryItemReach) } returns Unit
		coEvery { companiesHouseRepository.logScreenView(any()) } returns Unit
		coEvery { companiesHouseRepository.favourites() } returns listOf(searchHistoryItemTui)

		favouritesExecutor = FavouritesExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

	}

	@Test
	fun `when remove favourite the repo removes favourite after delay`() = runTest(testCoroutineDispatcher) {

		favouritesStore = FavouritesStoreFactory(DefaultStoreFactory(), favouritesExecutor).create()
		val states = favouritesStore.states.test()
		advanceUntilIdle()

		favouritesStore.accept(FavouritesStore.Intent.InitPendingRemoval(FavouritesItem(searchHistoryItemTui)))
		states.last().favourites shouldContain FavouritesItem(searchHistoryItemTui, true)

		advanceUntilIdle()
		coVerify(exactly = 1) { companiesHouseRepository.removeFavourite(searchHistoryItemTui) }
	}

	@Test
	fun `when remove multiple favourites the repo removes all of them after delay`()
	= runTest(testCoroutineDispatcher) {

		coEvery { companiesHouseRepository.favourites() } returns searchHistoryItems
		favouritesStore = FavouritesStoreFactory(DefaultStoreFactory(), favouritesExecutor).create()
		val states = favouritesStore.states.test()
		advanceUntilIdle()

		favouritesStore.accept(FavouritesStore.Intent.InitPendingRemoval(FavouritesItem(searchHistoryItemTui)))
		favouritesStore.accept(FavouritesStore.Intent.InitPendingRemoval(FavouritesItem(searchHistoryItemReach)))
		states.last().favourites shouldContain FavouritesItem(searchHistoryItemTui, true)
		states.last().favourites shouldContain FavouritesItem(searchHistoryItemReach, true)

		advanceUntilIdle()
		coVerify(exactly = 1) { companiesHouseRepository.removeFavourite(searchHistoryItemTui) }
		coVerify(exactly = 1) { companiesHouseRepository.removeFavourite(searchHistoryItemReach) }

	}

	@Test
	fun `when undo remove favourite the repo does not remove favourite`() = runTest(testCoroutineDispatcher) {
		favouritesStore = FavouritesStoreFactory(DefaultStoreFactory(), favouritesExecutor).create()
		val states = favouritesStore.states.test()
		advanceUntilIdle()

		favouritesStore.accept(FavouritesStore.Intent.InitPendingRemoval(FavouritesItem(searchHistoryItemTui)))
		states.last().favourites shouldContain FavouritesItem(searchHistoryItemTui, true)
		favouritesStore.accept(FavouritesStore.Intent.CancelPendingRemoval(FavouritesItem(searchHistoryItemTui, true)))
		states.last().favourites shouldContain FavouritesItem(searchHistoryItemTui, false)

		advanceUntilIdle()
		coVerify(exactly = 0) { companiesHouseRepository.removeFavourite(searchHistoryItemTui) }
	}

	@Test
	fun `when remove favourite and leaving the screen the repo removes favourite`() = runTest(testCoroutineDispatcher) {

		coEvery { companiesHouseRepository.favourites() } returns searchHistoryItems
		favouritesStore = FavouritesStoreFactory(DefaultStoreFactory(), favouritesExecutor).create()
		val labels = favouritesStore.labels.test()
		advanceUntilIdle()

		favouritesStore.accept(FavouritesStore.Intent.InitPendingRemoval(FavouritesItem(searchHistoryItemTui)))
		favouritesStore.accept(FavouritesStore.Intent.ExpeditePendingRemovals)
		advanceUntilIdle()

		verify(exactly = 1) { companiesHouseRepository.removeFavourite(any()) }
		labels.last() shouldBe FavouritesStore.SideEffect.Back

	}

}
