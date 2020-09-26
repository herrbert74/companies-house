package com.babestudios.companyinfouk.data.model.officers


import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.common.MonthYearDto
import com.babestudios.companyinfouk.data.model.common.SelfLinkDataDto
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

class OfficersResponseDto(
		@SerializedName("kind")
		var kind: String? = null,
		@SerializedName("items_per_page")
		var itemsPerPage: Int = 0,
		@SerializedName("total_results")
		var totalResults: Int = 0,
		@SerializedName("active_count")
		var activeCount: Int = 0,
		@SerializedName("start_index")
		var startIndex: Int = 0,
		@SerializedName("etag")
		var etag: String? = null,
		@SerializedName("items")
		var items: List<OfficerDto> = ArrayList(),
		@SerializedName("resigned_count")
		var resignedCount: Int = 0,
		@SerializedName("links")
		var links: SelfLinkDataDto? = null
)

@Parcelize
class OfficerDto(
		@SerializedName("address")
		var address: AddressDto? = null,
		@SerializedName("identification")
		var identification: IdentificationDto? = null,
		@SerializedName("appointed_on")
		var appointedOn: String? = null,
		@SerializedName("links")
		var links: OfficerLinksDto? = null,
		@SerializedName("name")
		var name: String? = null,
		@SerializedName("officer_role")
		var officerRole: String? = null,
		@SerializedName("date_of_birth")
		var dateOfBirth: MonthYearDto? = null,
		@SerializedName("occupation")
		var occupation: String = "Unknown",
		@SerializedName("country_of_residence")
		var countryOfResidence: String = "Unknown",
		@SerializedName("nationality")
		var nationality: String = "Unknown",
		@SerializedName("resigned_on")
		var resignedOn: String? = null
) : Parcelable

@Parcelize
class OfficerLinksDto(
		@SerializedName("officer")
		var officer: OfficerRelatedLinksDto? = null
) : Parcelable


@Parcelize
class OfficerRelatedLinksDto(
		@SerializedName("appointments")
		var appointments: String? = null
) : Parcelable

@Parcelize
class IdentificationDto(
		@SerializedName("place_registered")
		var placeRegistered: String? = null,
		@SerializedName("identification_type")
		var identificationType: String? = null,
		@SerializedName("registration_number")
		var registrationNumber: String? = null
) : Parcelable
