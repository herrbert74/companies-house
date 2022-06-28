package com.babestudios.companyinfouk.insolvencies

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.ext.test
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesExecutor
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStore
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStoreFactory
import com.github.michaelbull.result.Ok
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class InsolvenciesTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var insolvenciesExecutor: InsolvenciesExecutor

	private lateinit var insolvenciesStore: InsolvenciesStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@Before
	fun setUp() {
		coEvery {
			companiesHouseRepository.logScreenView(any())
		} answers { }

		coEvery {
			companiesHouseRepository.getInsolvency("123")
		} answers { Ok(Insolvency(cases = emptyList())) }

		insolvenciesExecutor = InsolvenciesExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		insolvenciesStore =
			InsolvenciesStoreFactory(DefaultStoreFactory(), insolvenciesExecutor).create(
				companyNumber = "123"
			)
	}

	@Test
	fun `when get insolvencies then repo get insolvencies is called`() {
		val states = insolvenciesStore.states.test()
		states.last().shouldBeTypeOf<InsolvenciesStore.State.Show>()
		coVerify(exactly = 1) { companiesHouseRepository.getInsolvency("123") }
		(states.last() as? InsolvenciesStore.State.Show)?.insolvencies shouldBe emptyList()
	}

}
