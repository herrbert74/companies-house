package com.babestudios.companyinfouk.shared.data.mapper

import com.babestudios.base.data.mapNullInputList
import com.babestudios.companyinfouk.shared.data.local.apilookup.ConstantsHelper
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

fun InsolvencyDto.toInsolvency() =
	Insolvency(
		mapNullInputList(cases) { case ->
			case.toInsolvencyCase(
				{ dates ->
					mapNullInputList(dates) { date -> date.toDate() }
				},
			) { practitioners ->
				mapNullInputList(practitioners) { practitioner ->
					practitioner.toPractitioner { addressDto -> addressDto.toAddress() }
				}
			}
		}
	)

fun InsolvencyCaseDto?.toInsolvencyCase(
	mapDateDto: (List<DateDto>?) -> List<Date>,
	mapPractitionerDto: (List<PractitionerDto>?) -> List<Practitioner>,
): InsolvencyCase {
	return InsolvencyCase(
		mapDateDto(this?.dates),
		mapPractitionerDto(this?.practitioners),
		ConstantsHelper.insolvencyCaseType(this?.type ?: ""),
	)
}

fun DateDto?.toDate() = Date(
	this?.date.orEmpty(),
	ConstantsHelper.insolvencyCaseDateType(this?.type ?: ""),
)

fun PractitionerDto?.toPractitioner(mapAddressDto: (AddressDto?) -> Address) = Practitioner(
	mapAddressDto(this?.address),
	this?.appointedOn,
	this?.ceasedToActOn,
	this?.name ?: "",
	this?.role,
)
