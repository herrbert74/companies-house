package com.babestudios.companyinfouk.data.mappers

import com.babestudios.base.data.mapNullInputList
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.shared.data.model.common.AddressDto
import com.babestudios.companyinfouk.shared.data.model.insolvency.DateDto
import com.babestudios.companyinfouk.shared.data.model.insolvency.InsolvencyCaseDto
import com.babestudios.companyinfouk.shared.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.shared.data.model.insolvency.PractitionerDto
import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Date
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.shared.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Practitioner

fun mapInsolvencyDto(input: InsolvencyDto, constantsHelper: ConstantsHelperContract) =
	Insolvency(
		mapNullInputList(input.cases) { case ->
			mapInsolvencyCaseDto(
				case,
				{ dates ->
					mapNullInputList(dates) { date -> mapDateDto(date, constantsHelper) }
				},
				{ practitioners ->
					mapNullInputList(practitioners) { practitioner ->
						mapPractitionerDto(practitioner) { addressDto -> mapAddressDto(addressDto) }
					}
				},
				constantsHelper,
			)
		}
	)

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
