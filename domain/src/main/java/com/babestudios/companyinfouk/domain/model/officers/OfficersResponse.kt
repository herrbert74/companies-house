package com.babestudios.companyinfouk.domain.model.officers

import android.os.Parcelable
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.common.MonthYear
import kotlinx.parcelize.Parcelize

data class OfficersResponse(
	val totalResults: Int = 0,
	val items: List<Officer> = emptyList(),
)

@Parcelize
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
) : Parcelable

@Parcelize
data class OfficerLinks(
	val officer: OfficerRelatedLinks? = null
) : Parcelable

@Parcelize
data class OfficerRelatedLinks(
	val appointments: String? = null
) : Parcelable
