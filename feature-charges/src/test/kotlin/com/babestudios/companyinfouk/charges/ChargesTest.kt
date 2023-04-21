package com.babestudios.companyinfouk.charges

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.charges.ui.charges.ChargesExecutor
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStoreFactory
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.charges.Charges
import com.github.michaelbull.result.Ok
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class ChargesTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var chargesExecutor: ChargesExecutor

	private lateinit var chargesStore: ChargesStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@Before
	fun setUp() {
		coEvery { companiesHouseRepository.logScreenView(any()) } answers { }

		coEvery {
			companiesHouseRepository.getCharges("123", "0")
		} answers { Ok(Charges()) }

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
		coVerify(exactly = 1) { companiesHouseRepository.getCharges("123", "0") }
	}

	@Test
	fun `when load more charges then repo load more charges is called`() {
		val states = chargesStore.states.test()
		chargesStore.init()
		chargesStore.accept(ChargesStore.Intent.LoadMoreCharges)

		states.last().isLoading shouldBe false
		states.last().chargesResponse shouldBe Charges()
		coVerify(exactly = 1) { companiesHouseRepository.getCharges("123", "0") }
	}

}
