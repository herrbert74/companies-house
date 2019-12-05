package com.babestudios.companyinfouk.persons.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.data.model.persons.Person
import com.babestudios.companyinfouk.data.model.persons.Persons
import com.babestudios.companyinfouk.persons.ui.persons.list.AbstractPersonsVisitable

data class PersonsState(
		//Persons
		val personsRequest: Async<Persons> = Uninitialized,
		val persons: List<AbstractPersonsVisitable> = emptyList(),
		val totalPersonCount: Long = 0,
		@PersistState
		val companyNumber: String = "",

		//Person details
		@PersistState
		val personItem: Person? = null

) : MvRxState