package com.babestudios.companyinfouk.data.mappers

import com.babestudios.base.data.mapNullInputList
import com.babestudios.companyinfouk.data.local.apilookup.PscHelper
import com.babestudios.companyinfouk.shared.data.model.persons.PersonDto
import com.babestudios.companyinfouk.shared.data.model.persons.PersonsResponseDto
import com.babestudios.companyinfouk.shared.domain.model.persons.Identification
import com.babestudios.companyinfouk.shared.domain.model.persons.Person
import com.babestudios.companyinfouk.shared.domain.model.persons.PersonsResponse

fun PersonsResponseDto.toPersonsResponse() = PersonsResponse(
	startIndex ?: 0,
	activeCount ?: 0,
	items.mapPersonsList(),
	ceasedCount ?: 0,
	itemsPerPage ?: 0,
	totalResults ?: 0,
)

private fun List<PersonDto>?.mapPersonsList(): List<Person> = mapNullInputList(this, PersonDto::toPerson)

fun PersonDto.toPerson() = Person(
	notifiedOn.orEmpty(),
	ceasedOn,
	PscHelper.kindLookUp(kind.orEmpty()),
	countryOfResidence,
	dateOfBirth.toMonthYear(),
	address.toAddress(),
	naturesOfControl.map { PscHelper.shortDescriptionLookUp(it) },
	nationality,
	name.orEmpty(),
	with(identification) {
		Identification(
			this?.countryRegistered,
			this?.legalAuthority.orEmpty(),
			this?.legalForm.orEmpty(),
			this?.placeRegistered,
			this?.registrationNumber
		)
	}
)
