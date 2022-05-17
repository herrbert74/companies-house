package com.babestudios.companyinfouk.persons.ui.persons

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.domain.model.persons.PersonsResponse
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.Intent
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.State

interface PersonsStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class PersonsItemClicked(val selectedPerson: Person) : Intent()
		data class LoadMorePersons(val page: Int) : Intent()
	}

	sealed class State {

		object Loading : State()

		class Show(
			val companyNumber: String,
			val personsResponse: PersonsResponse,
		) : State()

		class Error(val t: Throwable) : State()

	}

}
