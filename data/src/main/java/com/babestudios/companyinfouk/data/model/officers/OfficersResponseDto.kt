package com.babestudios.companyinfouk.data.model.officers

import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.common.MonthYearDto
import com.babestudios.companyinfouk.data.model.common.SelfLinkDataDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class OfficersResponseDto(
	@SerialName("kind")
	var kind: String? = null,
	@SerialName("items_per_page")
	var itemsPerPage: Int = 0,
	@SerialName("total_results")
	var totalResults: Int = 0,
	@SerialName("active_count")
	var activeCount: Int = 0,
	@SerialName("inactive_count")
	var inactiveCount: Int = 0,
	@SerialName("start_index")
	var startIndex: Int = 0,
	@SerialName("etag")
	var etag: String? = null,
	@SerialName("items")
	var items: List<OfficerDto> = ArrayList(),
	@SerialName("resigned_count")
	var resignedCount: Int = 0,
	@SerialName("links")
	var links: SelfLinkDataDto? = null,
)

@Serializable
class OfficerDto(
	@SerialName("address")
	var address: AddressDto? = null,
	@SerialName("identification")
	var identification: IdentificationDto? = null,
	@SerialName("appointed_on")
	var appointedOn: String? = null,
	@SerialName("links")
	var links: OfficerLinksDto? = null,
	@SerialName("name")
	var name: String? = null,
	@SerialName("officer_role")
	var officerRole: String? = null,
	@SerialName("date_of_birth")
	var dateOfBirth: MonthYearDto? = null,
	@SerialName("occupation")
	var occupation: String = "Unknown",
	@SerialName("country_of_residence")
	var countryOfResidence: String = "Unknown",
	@SerialName("nationality")
	var nationality: String = "Unknown",
	@SerialName("resigned_on")
	var resignedOn: String? = null,
)

@Serializable
class OfficerLinksDto(
	@SerialName("officer")
	var officer: OfficerRelatedLinksDto? = null,
)

@Serializable
class OfficerRelatedLinksDto(
	@SerialName("appointments")
	var appointments: String? = null,
)

@Serializable
class IdentificationDto(
	@SerialName("place_registered")
	var placeRegistered: String? = null,
	@SerialName("identification_type")
	var identificationType: String? = null,
	@SerialName("registration_number")
	var registrationNumber: String? = null,
)
