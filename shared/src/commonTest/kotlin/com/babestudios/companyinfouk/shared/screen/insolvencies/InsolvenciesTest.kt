package com.babestudios.companyinfouk.shared.screen.insolvencies

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Insolvency
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

class InsolvenciesTest {

	private val companiesHouseRepository = mock<CompaniesRepository>()

	private lateinit var insolvenciesExecutor: InsolvenciesExecutor

	private lateinit var insolvenciesStore: InsolvenciesStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@BeforeTest
	fun setUp() {
		every {
			companiesHouseRepository.logScreenView(any())
		} calls { }

		everySuspend {
			companiesHouseRepository.getInsolvency("123")
		} calls { Ok(Insolvency(cases = emptyList())) }

		insolvenciesExecutor = InsolvenciesExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		insolvenciesStore =
			InsolvenciesStoreFactory(DefaultStoreFactory(), insolvenciesExecutor).create(
				selectedCompanyId = "123"
			)
	}

	@Test
	fun `when get insolvencies then repo get insolvencies is called`() {
		val states = insolvenciesStore.states.test()

		verifySuspend(exactly(1)) { companiesHouseRepository.getInsolvency("123") }
		states.last().insolvency.cases shouldBe emptyList()
	}

}
