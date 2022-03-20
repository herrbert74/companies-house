package com.babestudios.companyinfouk.data.mappers

import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.insolvency.Date
import com.babestudios.companyinfouk.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.insolvency.DateDto
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCaseDto
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.data.model.insolvency.PractitionerDto

inline fun mapInsolvencyDto(
	input: InsolvencyDto,
	mapInsolvencyCase: (List<InsolvencyCaseDto>?) -> List<InsolvencyCase>,
): Insolvency {
	return Insolvency(
			mapInsolvencyCase(input.cases)
	)
}

fun mapInsolvencyCaseDto(
	input: InsolvencyCaseDto?,
	mapDateDto: (List<DateDto>?) -> List<Date>,
	mapPractitionerDto: (List<PractitionerDto>?) -> List<Practitioner>,
	constantsHelper: ConstantsHelperContract,
): InsolvencyCase {
	return InsolvencyCase(
			mapDateDto(input?.dates),
			mapPractitionerDto(input?.practitioners),
			constantsHelper.insolvencyCaseType(input?.type ?: ""),
	)
}

fun mapDateDto(
		input: DateDto?,
		constantsHelper: ConstantsHelperContract,
): Date {
	return Date(
			input?.date.orEmpty(),
			constantsHelper.insolvencyCaseDateType(input?.type ?: ""),
	)
}

fun mapPractitionerDto(
	input: PractitionerDto?,
	mapAddressDto: (AddressDto?) -> Address,
): Practitioner {
	return Practitioner(
			mapAddressDto(input?.address),
			input?.appointedOn,
			input?.ceasedToActOn,
			input?.name ?: "",
			input?.role,
	)
}
