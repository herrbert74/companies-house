package com.babestudios.companyinfouk.charges

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.charges.ui.charges.ChargesExecutor
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStoreFactory
import com.babestudios.companyinfouk.common.ext.test
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.charges.Charges
import com.github.michaelbull.result.Ok
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class ChargesTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var filingHistoryExecutor: ChargesExecutor

	private lateinit var chargesStore: ChargesStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@Before
	fun setUp() {
		coEvery { companiesHouseRepository.logScreenView(any()) } answers { }

		coEvery {
			companiesHouseRepository.getCharges("123", "0")
		} answers { Ok(Charges()) }

		filingHistoryExecutor = ChargesExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		chargesStore = ChargesStoreFactory(DefaultStoreFactory(), filingHistoryExecutor).create(
			companyNumber = "123", false
		)
	}

	@Test
	fun `when get charges then repo get charges is called`() {
		val states = chargesStore.states.test()
		states.first().shouldBeTypeOf<ChargesStore.State.Loading>()
		chargesStore.init()
		states.last().shouldBeTypeOf<ChargesStore.State.Show>()
		(states.last() as? ChargesStore.State.Show)?.charges shouldBe Charges()
		coVerify(exactly = 1) { companiesHouseRepository.getCharges("123", "0") }
	}

	@Test
	fun `when load more charges then repo load more charges is called`() {
		val states = chargesStore.states.test()
		chargesStore.init()
		chargesStore.accept(ChargesStore.Intent.LoadMoreCharges(1))

		states.last().shouldBeTypeOf<ChargesStore.State.Show>()
		(states.last() as? ChargesStore.State.Show)?.charges shouldBe Charges()
		coVerify(exactly = 1) { companiesHouseRepository.getCharges("123", "0") }
	}

}
