package com.babestudios.companyinfouk.officers

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.ext.test
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.officers.ui.officers.OfficersExecutor
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStoreFactory
import com.github.michaelbull.result.Ok
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
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

		officersStore = OfficersStoreFactory(DefaultStoreFactory(), officersExecutor).create("123")
	}

	@Test
	fun `when get officers then repo get officers is called`() {
		val states = officersStore.states.test()
		states.last().shouldBeTypeOf<OfficersStore.State.Show>()
		(states.last() as? OfficersStore.State.Show)?.officersResponse shouldBe OfficersResponse()
		coVerify(exactly = 1) { companiesHouseRepository.logScreenView("OfficersFragment") }
		coVerify(exactly = 1) { companiesHouseRepository.getOfficers("123", "0") }
	}

	@Test
	fun `when load more officers then repo load more officers is called`() {
		val states = officersStore.states.test()
		officersStore.accept(OfficersStore.Intent.LoadMoreOfficers(1))
		states.last().shouldBeTypeOf<OfficersStore.State.Show>()
		(states.last() as? OfficersStore.State.Show)?.officersResponse shouldBe OfficersResponse()
		coVerify(exactly = 1) { companiesHouseRepository.getOfficers("123", "0") }
	}

//	private fun officersViewModel(): OfficersViewModel {
//		return OfficersViewModel(
//			OfficersState(
//				companyNumber = "123",
//				selectedOfficerId = "123",
//				totalOfficersCount = 100,
//				totalAppointmentsCount = 200
//			),
//			companiesHouseRepository,
//			officersNavigator
//		)
//	}

}
