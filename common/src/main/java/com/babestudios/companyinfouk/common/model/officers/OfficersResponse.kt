package com.babestudios.companyinfouk.common.model.officers

import com.babestudios.companyinfouk.common.model.common.Address
import com.babestudios.companyinfouk.common.model.common.MonthYear

data class OfficersResponse(
		val totalResults: Int = 0,
		val items: List<Officer> = emptyList(),
)

data class Officer(
		val address: Address = Address(),
		val appointedOn: String = "",
		val links: OfficerLinks = OfficerLinks(),
		val name: String = "",
		val officerRole: String = "",
		val dateOfBirth: MonthYear = MonthYear(),
		val occupation: String = "Unknown",
		val countryOfResidence: String = "Unknown",
		val nationality: String = "Unknown",
		val resignedOn: String? = null,
		
		val fromToString: String,
)

data class OfficerLinks(
		val officer: OfficerRelatedLinks? = null
)

data class OfficerRelatedLinks(
		val appointments: String? = null
)
