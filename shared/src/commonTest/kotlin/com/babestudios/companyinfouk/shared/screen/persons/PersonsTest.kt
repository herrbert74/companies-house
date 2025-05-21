package com.babestudios.companyinfouk.shared.screen.persons

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.persons.PersonsResponse
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

class PersonsTest {

	private val companiesHouseRepository = mock<CompaniesRepository>()

	private lateinit var filingHistoryExecutor: PersonsExecutor

	private lateinit var personsStore: PersonsStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@BeforeTest
	fun setUp() {
		every {
			companiesHouseRepository.logScreenView(any())
		} calls { }

		everySuspend {
			companiesHouseRepository.getPersons("123", "0")
		} calls { Ok(PersonsResponse()) }

		filingHistoryExecutor = PersonsExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		personsStore =
			PersonsStoreFactory(DefaultStoreFactory(), filingHistoryExecutor).create(
				companyId = "123"
			)
	}

	@Test
	fun `when get persons then repo get persons is called`() {
		val states = personsStore.states.test()

		states.last().personsResponse shouldBe PersonsResponse()
		verifySuspend(exactly(1)) { companiesHouseRepository.getPersons("123", "0") }
	}

	@Test
	fun `when load more persons then repo load more persons is called`() {
		val states = personsStore.states.test()
		personsStore.accept(PersonsStore.Intent.LoadMorePersons)

		states.last().personsResponse shouldBe PersonsResponse()
		verifySuspend(exactly(1)) { companiesHouseRepository.getPersons("123", "0") }
	}

}
