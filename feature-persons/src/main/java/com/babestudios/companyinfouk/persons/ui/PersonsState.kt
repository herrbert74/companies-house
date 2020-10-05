package com.babestudios.companyinfouk.persons.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.common.model.persons.Person
import com.babestudios.companyinfouk.common.model.persons.PersonsResponse
import com.babestudios.companyinfouk.persons.ui.persons.list.PersonsVisitableBase

data class PersonsState(
		//Persons
		val personsRequest: Async<PersonsResponse> = Uninitialized,
		val persons: List<PersonsVisitableBase> = emptyList(),
		val totalPersonCount: Long = 0,
		@PersistState
		val companyNumber: String = "",

		//Person details
		@PersistState
		val personItem: Person? = null

) : MvRxState
