package com.babestudios.companyinfouk.persons.ui.persons.list

import com.babestudios.companyinfouk.domain.model.persons.Person

sealed class PersonsVisitableBase {
	abstract fun type(personsTypeFactory: PersonsAdapter.PersonsTypeFactory): Int
}

class PersonsVisitable(val person: Person) : PersonsVisitableBase() {
	override fun type(personsTypeFactory: PersonsAdapter.PersonsTypeFactory): Int {
		return personsTypeFactory.type(person)
	}
}
