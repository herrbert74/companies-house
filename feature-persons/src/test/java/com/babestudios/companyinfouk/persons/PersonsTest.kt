package com.babestudios.companyinfouk.persons

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.ext.test
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.persons.PersonsResponse
import com.babestudios.companyinfouk.persons.ui.persons.PersonsExecutor
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStoreFactory
import com.github.michaelbull.result.Ok
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class PersonsTest {

	private val companiesHouseRepository = mockk<CompaniesRepository>()

	private lateinit var filingHistoryExecutor: PersonsExecutor

	private lateinit var personsStore: PersonsStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@Before
	fun setUp() {
		coEvery {
			companiesHouseRepository.logScreenView(any())
		} answers { }

		coEvery {
			companiesHouseRepository.getPersons("123", "0")
		} answers { Ok(PersonsResponse()) }

		filingHistoryExecutor = PersonsExecutor(
			companiesHouseRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		personsStore =
			PersonsStoreFactory(DefaultStoreFactory(), filingHistoryExecutor).create(
				companyNumber = "123"
			)
	}

	@Test
	fun `when get persons then repo get persons is called`() {
		val states = personsStore.states.test()
		states.last().shouldBeTypeOf<PersonsStore.State.Show>()
		(states.last() as? PersonsStore.State.Show)?.personsResponse shouldBe PersonsResponse()
		coVerify(exactly = 1) { companiesHouseRepository.getPersons("123", "0") }
	}

	@Test
	fun `when load more persons then repo load more persons is called`() {
		val states = personsStore.states.test()
		personsStore.accept(PersonsStore.Intent.LoadMorePersons)
		states.last().shouldBeTypeOf<PersonsStore.State.Show>()
		(states.last() as? PersonsStore.State.Show)?.personsResponse shouldBe PersonsResponse()
		coVerify(exactly = 1) { companiesHouseRepository.getPersons("123", "0") }
	}

}
