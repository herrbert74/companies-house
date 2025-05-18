package com.babestudios.companyinfouk.shared.screen.filings

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistory
import com.github.michaelbull.result.Ok
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
import kotlinx.coroutines.test.runTest

class FilingHistoryTest {

	private val companiesHouseRepository = mock<CompaniesRepository>()

	private lateinit var filingHistoryExecutor: FilingHistoryExecutor

	private lateinit var filingHistoryStore: FilingHistoryStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@BeforeTest
	fun setUp() {
		everySuspend {
			companiesHouseRepository.getFilingHistory("123", any(), any())
		} calls  { Ok(FilingHistory()) }

		every {
			companiesHouseRepository.logScreenView(any())
		} calls { }

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
	fun `when get filings then repo get filings is called`() = runTest{

		val states = filingHistoryStore.states.test()
		states.first().isLoading shouldBe true
		filingHistoryStore.init()
		states.last().filingHistory shouldBe FilingHistory()
		verifySuspend(exactly(1)) { companiesHouseRepository.getFilingHistory("123", any(), "0") }

	}

	@Test
	fun `when load more filings then repo load more filings is called`() {

		val states = filingHistoryStore.states.test()
		filingHistoryStore.init()
		filingHistoryStore.accept(FilingHistoryStore.Intent.LoadMoreFilingHistory)
		states.last().filingHistory shouldBe FilingHistory()
		verifySuspend(exactly(1)) {
			companiesHouseRepository.getFilingHistory(
				"123", any(), "0"
			)
		}

	}

}
