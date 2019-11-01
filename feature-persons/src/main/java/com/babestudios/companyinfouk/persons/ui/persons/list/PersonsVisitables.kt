package com.babestudios.companyinfouk.persons.ui.persons.list

import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.persons.Person
import kotlinx.android.parcel.Parcelize

abstract class AbstractPersonsVisitable : Parcelable {
	abstract fun type(personsTypeFactory: PersonsAdapter.PersonsTypeFactory): Int
}

@Parcelize
class PersonsVisitable(val person: Person) : AbstractPersonsVisitable(), Parcelable {
	override fun type(personsTypeFactory: PersonsAdapter.PersonsTypeFactory): Int {
		return personsTypeFactory.type(person)
	}
}