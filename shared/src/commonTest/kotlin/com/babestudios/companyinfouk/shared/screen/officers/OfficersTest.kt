package com.babestudios.companyinfouk.shared.screen.officers

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficersResponse
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

class OfficersTest {

	private val companiesHouseRepository = mock<CompaniesRepository>()

	private lateinit var officersExecutor: OfficersExecutor

	private lateinit var officersStore: OfficersStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@BeforeTest
	fun setUp() {
		every { companiesHouseRepository.logScreenView(any()) } calls { }

		everySuspend {
			companiesHouseRepository.getOfficers("123", "0")
		} calls
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
		verifySuspend(exactly(1)) { companiesHouseRepository.logScreenView("OfficersFragment") }
		verifySuspend(exactly(1)) { companiesHouseRepository.getOfficers("123", "0") }
	}

	@Test
	fun `when load more officers then repo load more officers is called`() {
		val states = officersStore.states.test()
		officersStore.init()

		officersStore.accept(OfficersStore.Intent.LoadMoreOfficers)
		states.last().isLoading shouldBe false
		states.last().officersResponse shouldBe OfficersResponse()
		verifySuspend(exactly(1)) { companiesHouseRepository.getOfficers("123", "0") }
	}

}
