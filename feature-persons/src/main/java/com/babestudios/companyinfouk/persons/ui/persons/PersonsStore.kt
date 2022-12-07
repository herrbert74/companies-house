package com.babestudios.companyinfouk.persons.ui.persons

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.persons.PersonsResponse
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.Intent
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.State

interface PersonsStore : Store<Intent, State, Nothing> {

	sealed class Intent {
		object LoadMorePersons : Intent()
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
