package com.babestudios.companyinfouk.data.mappers

import com.babestudios.base.data.mapNullInputList
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.data.utils.StringResourceHelper
import com.babestudios.companyinfouk.shared.data.model.common.MonthYearDto
import com.babestudios.companyinfouk.shared.data.model.officers.OfficerDto
import com.babestudios.companyinfouk.shared.data.model.officers.OfficerLinksDto
import com.babestudios.companyinfouk.shared.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.shared.domain.model.common.MonthYear
import com.babestudios.companyinfouk.shared.domain.model.officers.Officer
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficerLinks
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficerRelatedLinks
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficersResponse
import java.util.regex.Pattern

fun OfficersResponseDto.toOfficersResponse() = OfficersResponse(totalResults, items.mapOfficerList())

private fun List<OfficerDto>?.mapOfficerList() = mapNullInputList(this, OfficerDto::toOfficer)

private fun OfficerDto?.toOfficer(): Officer {
	val appointedOn = this?.appointedOn ?: "Unknown"
	val resignedOn = this?.resignedOn
	return Officer(
		this?.address.toAddress(),
		appointedOn,
		this?.links.toOfficerLinks(),
		this?.name ?: "",
		ConstantsHelper.officerRoleLookup(this?.officerRole ?: ""),
		this?.dateOfBirth.toMonthYear(),
		this?.occupation ?: "Unknown",
		this?.countryOfResidence ?: "Unknown",
		this?.nationality ?: "Unknown",
		resignedOn,
		if (resignedOn.isNullOrEmpty()) StringResourceHelper.getAppointedFromString(appointedOn) else
			StringResourceHelper.getAppointedFromToString(appointedOn, resignedOn),
		extractOfficerAppointmentsId(this?.links?.officer?.appointments ?: "")
	)
}

private fun extractOfficerAppointmentsId(appointmentsUrl: String): String {
	val pattern = Pattern.compile("officers/(.+)/appointments")
	val matcher = pattern.matcher(appointmentsUrl)
	var officerId = ""
	if (matcher.find()) {
		officerId = matcher.group(1) ?: ""
	}
	return officerId
}

private fun OfficerLinksDto?.toOfficerLinks() =
	OfficerLinks(
		OfficerRelatedLinks(
			this?.officer?.appointments ?: ""
		)
	)

internal fun MonthYearDto?.toMonthYear() = MonthYear(this?.year, this?.month)
