package com.babestudios.companyinfouk.persons.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.data.model.persons.Person
import com.babestudios.companyinfouk.data.model.persons.Persons
import com.babestudios.companyinfouk.persons.ui.persons.list.AbstractPersonsVisitable

data class PersonsState(
		//Persons
		val personsRequest: Async<Persons> = Uninitialized,
		var persons: List<AbstractPersonsVisitable> = emptyList(),
		var totalPersonCount: Long = 0,
		var companyNumber: String = "",

		//Person details
		val personItem: Person? = null

		) : MvRxState