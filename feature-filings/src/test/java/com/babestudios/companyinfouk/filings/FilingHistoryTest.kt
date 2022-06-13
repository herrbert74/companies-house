package com.babestudios.companyinfouk.filings

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.common.ext.test
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryExecutor
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryStore
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingsHistoryStoreFactory
import com.github.michaelbull.result.Ok
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class FilingHistoryTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var filingHistoryExecutor: FilingHistoryExecutor

	private lateinit var filingHistoryStore: FilingHistoryStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@Before
	fun setUp() {
		coEvery {
			companiesHouseRepository.getFilingHistory("123", any(), any())
		} answers { Ok(FilingHistory()) }

		coEvery {
			companiesHouseRepository.logScreenView(any())
		} answers { }

		filingHistoryExecutor = FilingHistoryExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		filingHistoryStore =
			FilingsHistoryStoreFactory(DefaultStoreFactory(), filingHistoryExecutor).create(
				companyNumber = "123", autoInit = false
			)

	}

	@Test
	fun `when get filings then repo get filings is called`() {

		val states = filingHistoryStore.states.test()
		states.first().shouldBeTypeOf<FilingHistoryStore.State.Loading>()
		filingHistoryStore.init()
		states.last().shouldBeTypeOf<FilingHistoryStore.State.Show>()
		(states.last() as? FilingHistoryStore.State.Show)?.filingHistory shouldBe FilingHistory()
		coVerify(exactly = 1) { companiesHouseRepository.getFilingHistory("123", any(), "0") }

	}

	@Test
	fun `when load more filings then repo load more filings is called`() {

		val states = filingHistoryStore.states.test()
		filingHistoryStore.init()
		filingHistoryStore.accept(FilingHistoryStore.Intent.LoadMoreFilingHistory(1))
		states.last().shouldBeTypeOf<FilingHistoryStore.State.Show>()
		(states.last() as? FilingHistoryStore.State.Show)?.filingHistory shouldBe FilingHistory()
		coVerify(exactly = 1) {
			companiesHouseRepository.getFilingHistory(
				"123", any(), BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE
			)
		}

	}

}
