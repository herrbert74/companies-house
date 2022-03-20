package com.babestudios.companyinfouk.data.mappers

import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.common.MonthYear
import com.babestudios.companyinfouk.domain.model.persons.Identification
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.domain.model.persons.PersonsResponse
import com.babestudios.companyinfouk.data.local.apilookup.PscHelperContract
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.common.MonthYearDto
import com.babestudios.companyinfouk.data.model.persons.PersonDto
import com.babestudios.companyinfouk.data.model.persons.PersonsResponseDto

inline fun mapPersonsResponseDto(
		input: PersonsResponseDto,
		mapPersonDto: (List<PersonDto>?) -> List<Person>
): PersonsResponse {
	return PersonsResponse(
			input.startIndex ?: 0,
			input.activeCount ?: 0,
			mapPersonDto(input.items),
			input.ceasedCount ?: 0,
			input.itemsPerPage ?: 0,
			input.totalResults ?: 0,
	)
}

fun mapPersonDto(
	input: PersonDto,
	pscHelper: PscHelperContract,
	mapAddressDto: (AddressDto?) -> Address,
	mapMonthYearDto: (MonthYearDto?) -> MonthYear,
): Person {
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
