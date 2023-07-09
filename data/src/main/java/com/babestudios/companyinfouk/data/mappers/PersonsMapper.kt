package com.babestudios.companyinfouk.data.mappers

import com.babestudios.base.data.mapNullInputList
import com.babestudios.companyinfouk.data.local.apilookup.PscHelperContract
import com.babestudios.companyinfouk.shared.data.model.persons.PersonDto
import com.babestudios.companyinfouk.shared.data.model.persons.PersonsResponseDto
import com.babestudios.companyinfouk.shared.domain.model.persons.Identification
import com.babestudios.companyinfouk.shared.domain.model.persons.Person
import com.babestudios.companyinfouk.shared.domain.model.persons.PersonsResponse

fun mapPersonsResponseDto(
	input: PersonsResponseDto,
	pscHelper: PscHelperContract
): PersonsResponse {
	return PersonsResponse(
		input.startIndex ?: 0,
		input.activeCount ?: 0,
		mapPersonsList(input.items, pscHelper),
		input.ceasedCount ?: 0,
		input.itemsPerPage ?: 0,
		input.totalResults ?: 0,
	)
}

private fun mapPersonsList(personList: List<PersonDto>?, pscHelper: PscHelperContract): List<Person> =
	mapNullInputList(personList) { personDto ->
		mapPersonDto(personDto, pscHelper)
	}

fun mapPersonDto(input: PersonDto, pscHelper: PscHelperContract): Person {
	return Person(
		input.notifiedOn.orEmpty(),
		input.ceasedOn,
		pscHelper.kindLookUp(input.kind.orEmpty()),
		input.countryOfResidence,
		mapMonthYearDto(input.dateOfBirth),
		mapAddressDto(input.address),
		input.naturesOfControl.map { pscHelper.shortDescriptionLookUp(it) },
		input.nationality,
		input.name.orEmpty(),
		with(input.identification) {
			Identification(
				this?.countryRegistered,
				this?.legalAuthority.orEmpty(),
				this?.legalForm.orEmpty(),
				this?.placeRegistered,
				this?.registrationNumber
			)
		}
	)
}
