package com.babestudios.companyinfouk.shared.screen.charges

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.charges.Charges
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

class ChargesTest {

	private val companiesHouseRepository = mock<CompaniesRepository>()

	private lateinit var chargesExecutor: ChargesExecutor

	private lateinit var chargesStore: ChargesStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@BeforeTest
	fun setUp() {
		every { companiesHouseRepository.logScreenView(any()) } calls { }

		everySuspend {
			companiesHouseRepository.getCharges("123", "0")
		} calls { Ok(Charges()) }

		chargesExecutor = ChargesExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		chargesStore = ChargesStoreFactory(DefaultStoreFactory(), chargesExecutor).create(
			selectedCompanyId = "123", false
		)
	}

	@Test
	fun `when get charges then repo get charges is called`() {
		val states = chargesStore.states.test()
		states.first().isLoading shouldBe true
		chargesStore.init()

		states.last().isLoading shouldBe false
		states.last().chargesResponse shouldBe Charges()
		verifySuspend(exactly(1)) { companiesHouseRepository.getCharges("123", "0") }
	}

	@Test
	fun `when load more charges then repo load more charges is called`() {
		val states = chargesStore.states.test()
		chargesStore.init()
		chargesStore.accept(ChargesStore.Intent.LoadMoreCharges)

		states.last().isLoading shouldBe false
		states.last().chargesResponse shouldBe Charges()
		verifySuspend(exactly(1)) { companiesHouseRepository.getCharges("123", "0") }
	}

}
