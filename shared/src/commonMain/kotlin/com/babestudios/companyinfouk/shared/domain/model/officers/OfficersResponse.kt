package com.babestudios.companyinfouk.shared.domain.model.officers

import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.common.MonthYear
import kotlinx.serialization.Serializable

data class OfficersResponse(
	val totalResults: Int = 0,
	val items: List<Officer> = emptyList(),
)

@Serializable
data class Officer(
	val address: Address = Address(),
	val appointedOn: String? = null,
	val links: OfficerLinks = OfficerLinks(),
	val name: String = "",
	val officerRole: String = "",
	val dateOfBirth: MonthYear = MonthYear(null, null),
	val occupation: String = "Unknown",
	val countryOfResidence: String = "Unknown",
	val nationality: String = "Unknown",
	val resignedOn: String? = null,
	val fromToString: String,
	val appointmentsId: String, //Not present in the Dto, converted from a url
)

@Serializable
data class OfficerLinks(val officer: OfficerRelatedLinks? = null)

@Serializable
data class OfficerRelatedLinks(val appointments: String? = null)
