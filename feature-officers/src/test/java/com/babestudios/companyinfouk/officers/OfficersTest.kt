package com.babestudios.companyinfouk.officers

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.shared.screen.officers.OfficersExecutor
import com.babestudios.companyinfouk.shared.screen.officers.OfficersStore
import com.babestudios.companyinfouk.shared.screen.officers.OfficersStoreFactory
import com.github.michaelbull.result.Ok
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class OfficersTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var officersExecutor: OfficersExecutor

	private lateinit var officersStore: OfficersStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@Before
	fun setUp() {
		coEvery { companiesHouseRepository.logScreenView(any()) } answers { }

		coEvery {
			companiesHouseRepository.getOfficers("123", "0")
		} answers
			{
				Ok(OfficersResponse())
			}

		officersExecutor = OfficersExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		officersStore = OfficersStoreFactory(DefaultStoreFactory(), officersExecutor).create("123", false)
	}

	@Test
	fun `when get officers then repo get officers is called`() {
		val states = officersStore.states.test()
		states.first().isLoading shouldBe true
		officersStore.init()

		states.last().isLoading shouldBe false
		states.last().officersResponse shouldBe OfficersResponse()
		coVerify(exactly = 1) { companiesHouseRepository.logScreenView("OfficersFragment") }
		coVerify(exactly = 1) { companiesHouseRepository.getOfficers("123", "0") }
	}

	@Test
	fun `when load more officers then repo load more officers is called`() {
		val states = officersStore.states.test()
		officersStore.init()

		officersStore.accept(OfficersStore.Intent.LoadMoreOfficers)
		states.last().isLoading shouldBe false
		states.last().officersResponse shouldBe OfficersResponse()
		coVerify(exactly = 1) { companiesHouseRepository.getOfficers("123", "0") }
	}

}
