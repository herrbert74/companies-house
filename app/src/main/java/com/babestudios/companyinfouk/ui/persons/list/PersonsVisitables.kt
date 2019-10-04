package com.babestudios.companyinfouk.ui.persons.list

import android.os.Parcelable
import com.babestudios.companyinfo.data.model.persons.Person
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