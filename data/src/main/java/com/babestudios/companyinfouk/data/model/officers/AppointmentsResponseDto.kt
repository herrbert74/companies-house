package com.babestudios.companyinfouk.data.model.officers

import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.common.MonthYearDto
import com.babestudios.companyinfouk.data.model.common.SelfLinkDataDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AppointmentsResponseDto {
	@SerialName("date_of_birth")
	var dateOfBirth: MonthYearDto? = null

	@SerialName("etag")
	var etag: String? = null

	@SerialName("is_corporate_officer")
	var isCorporateOfficer: Boolean? = null

	@SerialName("items")
	var items: List<AppointmentDto>? = null

	@SerialName("items_per_page")
	var itemsPerPage: Int = 0

	@SerialName("kind")
	var kind: String = ""

	@SerialName("links")
	var links: SelfLinkDataDto = SelfLinkDataDto("")

	@SerialName("name")
	var name: String = ""

	@SerialName("start_index")
	var startIndex: Int = 0

	@SerialName("total_results")
	var totalResults: Int = 0
}

@Serializable
data class AppointmentDto(
	@SerialName("address")
	var address: AddressDto? = null,
	@SerialName("appointed_before")
	var appointedBefore: String? = null,
	@SerialName("appointed_on")
	var appointedOn: String? = null,
	@SerialName("appointed_to")
	var appointedTo: AppointedToDto? = null,
	@SerialName("country_of_residence")
	var countryOfResidence: String? = null,
	@SerialName("former_names")
	var formerNames: List<FormerNameDto>? = null,
	@SerialName("identification")
	var identification: IdentificationDto? = null,
	@SerialName("is_pre_1992_appointment")
	var isPre1992Appointment: String? = null,
	@SerialName("links")
	var links: AppointmentLinksDto? = null,
	@SerialName("name")
	var name: String? = null,
	@SerialName("name_elements")
	var nameElements: NameElementsDto? = null,
	@SerialName("nationality")
	var nationality: String? = null,
	@SerialName("occupation")
	var occupation: String? = null,
	@SerialName("officer_role")
	var officerRole: String? = null,
	@SerialName("resigned_on")
	var resignedOn: String? = null,
)

@Serializable
class AppointedToDto(
	@SerialName("company_name")
	var companyName: String? = null,
	@SerialName("company_number")
	var companyNumber: String? = null,
	@SerialName("company_status")
	var companyStatus: String? = null,
)

@Serializable
class AppointmentLinksDto(
	@SerialName("company")
	var company: String? = null,
)

@Serializable
class FormerNameDto(
	@SerialName("forenames")
	var forenames: String? = null,
	@SerialName("surname")
	var surname: String? = null,
)

@Serializable
class NameElementsDto(
	@SerialName("forename")
	var forename: String? = null,
	@SerialName("honours")
	var honours: String? = null,
	@SerialName("other_forenames")
	var otherForenames: String? = null,
	@SerialName("surname")
	var surname: String? = null,
	@SerialName("title")
	var title: String? = null,
)
