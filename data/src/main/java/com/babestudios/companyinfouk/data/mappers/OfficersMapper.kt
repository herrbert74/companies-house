package com.babestudios.companyinfouk.data.mappers

import com.babestudios.base.data.mapNullInputList
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.common.MonthYearDto
import com.babestudios.companyinfouk.data.model.officers.OfficerDto
import com.babestudios.companyinfouk.data.model.officers.OfficerLinksDto
import com.babestudios.companyinfouk.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.data.utils.StringResourceHelperContract
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.common.MonthYear
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.domain.model.officers.OfficerLinks
import com.babestudios.companyinfouk.domain.model.officers.OfficerRelatedLinks
import com.babestudios.companyinfouk.domain.model.officers.OfficersResponse
import java.util.regex.Pattern

fun mapOfficersResponseDto(
	input: OfficersResponseDto,
	constantsHelper: ConstantsHelperContract,
	stringResourceHelper: StringResourceHelperContract,
): OfficersResponse {
	return OfficersResponse(
		input.totalResults,
		mapOfficerList(input.items, constantsHelper, stringResourceHelper)
	)
}

private fun mapOfficerList(
	officers: List<OfficerDto>?,
	constantsHelper: ConstantsHelperContract,
	stringResourceHelper: StringResourceHelperContract
) = mapNullInputList(officers) { officerDto ->
	mapOfficerDto(
		officerDto,
		{ linksDto -> mapOfficerLinksDto(linksDto) },
		{ registeredOfficeAddressDto -> mapAddressDto(registeredOfficeAddressDto) },
		{ monthYearDto -> mapMonthYearDto(monthYearDto) },
		constantsHelper,
		stringResourceHelper
	)
}

@Suppress("LongParameterList")
private fun mapOfficerDto(
	input: OfficerDto?,
	mapOfficerLinksDto: (OfficerLinksDto?) -> OfficerLinks,
	mapAddressDto: (AddressDto?) -> Address,
	mapMonthYearDto: (MonthYearDto?) -> MonthYear,
	constantsHelper: ConstantsHelperContract,
	stringResourceHelper: StringResourceHelperContract
): Officer {
	val appointedOn = input?.appointedOn ?: "Unknown"
	val resignedOn = input?.resignedOn
	return Officer(
		mapAddressDto(input?.address),
		appointedOn,
		mapOfficerLinksDto(input?.links),
		input?.name ?: "",
		constantsHelper.officerRoleLookup(input?.officerRole ?: ""),
		mapMonthYearDto(input?.dateOfBirth),
		input?.occupation ?: "Unknown",
		input?.countryOfResidence ?: "Unknown",
		input?.nationality ?: "Unknown",
		resignedOn,
		if (resignedOn.isNullOrEmpty()) stringResourceHelper.getAppointedFromString(appointedOn) else
			stringResourceHelper.getAppointedFromToString(appointedOn, resignedOn),
		extractOfficerAppointmentsId(input?.links?.officer?.appointments ?: "")
	)
}

private fun extractOfficerAppointmentsId(appointmentsUrl: String) : String {
	val pattern = Pattern.compile("officers/(.+)/appointments")
	val matcher = pattern.matcher(appointmentsUrl)
	var officerId = ""
	if (matcher.find()) {
		officerId = matcher.group(1) ?: ""
	}
	return officerId
}

private fun mapOfficerLinksDto(input: OfficerLinksDto?) =
	OfficerLinks(OfficerRelatedLinks(input?.officer?.appointments ?: ""))


internal fun mapMonthYearDto(input: MonthYearDto?): MonthYear {
	return MonthYear(input?.year, input?.month)
}
