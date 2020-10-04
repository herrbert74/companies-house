package com.babestudios.companyinfouk.data.model.officers


import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.common.MonthYearDto
import com.babestudios.companyinfouk.data.model.common.SelfLinkDataDto
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class AppointmentsResponseDto {
	@SerializedName("date_of_birth")
	var dateOfBirth: MonthYearDto? = null

	@SerializedName("etag")
	var etag: String? = null

	@SerializedName("is_corporate_officer")
	var isCorporateOfficer: String? = null

	@SerializedName("items")
	var items: List<AppointmentDto>? = null

	@SerializedName("items_per_page")
	var itemsPerPage: Int = 0

	@SerializedName("kind")
	var kind: String = ""

	@SerializedName("links")
	var links: SelfLinkDataDto = SelfLinkDataDto("")

	@SerializedName("name")
	var name: String = ""

	@SerializedName("start_index")
	var startIndex: Int = 0

	@SerializedName("total_results")
	var totalResults: Int = 0
}

@Parcelize
data class AppointmentDto(
		@SerializedName("address")
		var address: AddressDto? = null,
		@SerializedName("appointed_before")
		var appointedBefore: String? = null,
		@SerializedName("appointed_on")
		var appointedOn: String? = null,
		@SerializedName("appointed_to")
		var appointedTo: AppointedToDto? = null,
		@SerializedName("country_of_residence")
		var countryOfResidence: String? = null,
		@SerializedName("former_names")
		var formerNames: List<FormerNameDto>? = null,
		@SerializedName("identification")
		var identification: IdentificationDto? = null,
		@SerializedName("is_pre_1992_appointment")
		var isPre1992Appointment: String? = null,
		@SerializedName("links")
		var links: AppointmentLinksDto? = null,
		@SerializedName("name")
		var name: String? = null,
		@SerializedName("name_elements")
		var nameElements: NameElementsDto? = null,
		@SerializedName("nationality")
		var nationality: String? = null,
		@SerializedName("occupation")
		var occupation: String? = null,
		@SerializedName("officer_role")
		var officerRole: String? = null,
		@SerializedName("resigned_on")
		var resignedOn: String? = null
) : Parcelable

@Parcelize
class AppointedToDto(
		@SerializedName("company_name")
		var companyName: String? = null,
		@SerializedName("company_number")
		var companyNumber: String? = null,
		@SerializedName("company_status")
		var companyStatus: String? = null
) : Parcelable

@Parcelize
class AppointmentLinksDto(
		@SerializedName("company")
		var company: String? = null
) : Parcelable


@Parcelize
class FormerNameDto(
		@SerializedName("forenames")
		var forenames: String? = null,
		@SerializedName("surname")
		var surname: String? = null
) : Parcelable

@Parcelize
class NameElementsDto(
		@SerializedName("forename")
		var forename: String? = null,
		@SerializedName("honours")
		var honours: String? = null,
		@SerializedName("other_forenames")
		var otherForenames: String? = null,
		@SerializedName("surname")
		var surname: String? = null,
		@SerializedName("title")
		var title: String? = null
) : Parcelable
