package com.babestudios.companyinfouk.filings

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryExecutor
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryStore
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryStoreFactory
import com.github.michaelbull.result.Ok
import io.kotest.matchers.shouldBe
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

		filingHistoryStore = FilingHistoryStoreFactory(DefaultStoreFactory(), filingHistoryExecutor).create(
				selectedCompanyId = "123", autoInit = false
			)

	}

	@Test
	fun `when get filings then repo get filings is called`() {

		val states = filingHistoryStore.states.test()
		states.first().isLoading shouldBe true
		filingHistoryStore.init()
		states.last().filingHistory shouldBe FilingHistory()
		coVerify(exactly = 1) { companiesHouseRepository.getFilingHistory("123", any(), "0") }

	}

	@Test
	fun `when load more filings then repo load more filings is called`() {

		val states = filingHistoryStore.states.test()
		filingHistoryStore.init()
		filingHistoryStore.accept(FilingHistoryStore.Intent.LoadMoreFilingHistory)
		states.last().filingHistory shouldBe FilingHistory()
		coVerify(exactly = 1) {
			companiesHouseRepository.getFilingHistory(
				"123", any(), "0"
			)
		}

	}

}
