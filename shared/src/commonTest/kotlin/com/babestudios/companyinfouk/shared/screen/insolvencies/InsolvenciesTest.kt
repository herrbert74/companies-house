package com.babestudios.companyinfouk.shared.screen.insolvencies

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Insolvency
import com.github.michaelbull.result.Ok
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks

@UsesMocks(CompaniesRepository::class)
class InsolvenciesTest : TestsWithMocks() {

	@Mock
	lateinit var companiesHouseRepository: CompaniesRepository

	private lateinit var insolvenciesExecutor: InsolvenciesExecutor

	private lateinit var insolvenciesStore: InsolvenciesStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	override fun setUpMocks() = mocker.injectMocks(this)

	override fun initMocksBeforeTest() {
		super.initMocksBeforeTest()
		mocker.every {
			companiesHouseRepository.logScreenView(isAny())
		} returns Unit

		runBlocking(testCoroutineDispatcher) {
			mocker.everySuspending {
				companiesHouseRepository.getInsolvency("123")
			} returns Ok(Insolvency(cases = emptyList()))
		}

		insolvenciesExecutor = InsolvenciesExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		insolvenciesStore = InsolvenciesStoreFactory(DefaultStoreFactory(), insolvenciesExecutor)
			.create(selectedCompanyId = "123")

	}

	@Test
	fun `when get insolvencies then repo get insolvencies is called`() = runTest {

		val states = insolvenciesStore.states.test()

		mocker.verifyWithSuspend {
			companiesHouseRepository.logScreenView(isAny())
			companiesHouseRepository.getInsolvency("123")
		}
		states.last().insolvency.cases shouldBe emptyList()
	}

}
