package com.babestudios.companyinfouk.shared.screen.persons

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.shared.domain.model.persons.PersonsResponse
import com.babestudios.companyinfouk.shared.screen.persons.PersonsStore.Intent
import com.babestudios.companyinfouk.shared.screen.persons.PersonsStore.State

interface PersonsStore : Store<Intent, State, Nothing> {

	sealed class Intent {
		object LoadMorePersons : Intent()
	}

	data class State(

		// initial data
		val companyId: String,

		// result data
		val personsResponse: PersonsResponse = PersonsResponse(),
		val error: Throwable? = null,

		// state
		val isLoading: Boolean = true,

		)

}
