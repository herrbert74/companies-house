package com.babestudios.companyinfouk.persons.ui.persons

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.persons.PersonsResponse
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.Intent
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.State

interface PersonsStore : Store<Intent, State, Nothing> {

	sealed class Intent {
		object LoadMorePersons : Intent()
	}

	data class State(

		//initial data
		val companyId: String,

		//result data
		val personsResponse: PersonsResponse = PersonsResponse(),
		val error: Throwable? = null,

		//state
		val isLoading: Boolean = true,

		)

}
