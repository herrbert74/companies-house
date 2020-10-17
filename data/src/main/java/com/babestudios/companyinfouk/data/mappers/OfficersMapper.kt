package com.babestudios.companyinfouk.data.mappers

import com.babestudios.companyinfouk.common.model.common.Address
import com.babestudios.companyinfouk.common.model.common.MonthYear
import com.babestudios.companyinfouk.common.model.officers.Officer
import com.babestudios.companyinfouk.common.model.officers.OfficerLinks
import com.babestudios.companyinfouk.common.model.officers.OfficerRelatedLinks
import com.babestudios.companyinfouk.common.model.officers.OfficersResponse
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.common.MonthYearDto
import com.babestudios.companyinfouk.data.model.officers.OfficerDto
import com.babestudios.companyinfouk.data.model.officers.OfficerLinksDto
import com.babestudios.companyinfouk.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.data.utils.StringResourceHelperContract

inline fun mapOfficersResponseDto(
		input: OfficersResponseDto,
		mapOfficerDto: (List<OfficerDto>?) -> List<Officer>,
): OfficersResponse {
	return OfficersResponse(
			input.totalResults,
			mapOfficerDto(input.items)
	)
}

@Suppress("LongParameterList")
fun mapOfficerDto(
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
				stringResourceHelper.getAppointedFromToString(appointedOn, resignedOn)
	)
}

fun mapOfficerLinksDto(input: OfficerLinksDto?) =
		OfficerLinks(OfficerRelatedLinks(input?.officer?.appointments ?: ""))


fun mapMonthYearDto(input: MonthYearDto?): MonthYear {
	return MonthYear(input?.year, input?.month)
}
