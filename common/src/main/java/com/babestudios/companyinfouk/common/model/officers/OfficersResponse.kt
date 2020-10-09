package com.babestudios.companyinfouk.common.model.officers

import android.os.Parcelable
import com.babestudios.companyinfouk.common.model.common.Address
import com.babestudios.companyinfouk.common.model.common.MonthYear
import kotlinx.android.parcel.Parcelize

data class OfficersResponse(
		val totalResults: Int = 0,
		val items: List<Officer> = emptyList(),
)

@Parcelize
data class Officer(
		val address: Address = Address(),
		val appointedOn: String = "",
		val links: OfficerLinks = OfficerLinks(),
		val name: String = "",
		val officerRole: String = "",
		val dateOfBirth: MonthYear = MonthYear(null, null),
		val occupation: String = "Unknown",
		val countryOfResidence: String = "Unknown",
		val nationality: String = "Unknown",
		val resignedOn: String? = null,
		val fromToString: String,
) : Parcelable

@Parcelize
data class OfficerLinks(
		val officer: OfficerRelatedLinks? = null
) : Parcelable

@Parcelize
data class OfficerRelatedLinks(
		val appointments: String? = null
) : Parcelable
