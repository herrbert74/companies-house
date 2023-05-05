package com.babestudios.companyinfouk.data.model.persons

import androidx.annotation.Keep
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.common.MonthYearDto
import com.babestudios.companyinfouk.data.model.common.SelfLinkDataDto
import java.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PersonDto(
	@SerialName("notified_on")
	var notifiedOn: String? = null,
	@SerialName("ceased_on")
	var ceasedOn: String? = null,
	@SerialName("kind")
	var kind: String? = null,
	@SerialName("country_of_residence")
	var countryOfResidence: String? = null,
	@SerialName("etag")
	var etag: String? = null,
	@SerialName("date_of_birth")
	var dateOfBirth: MonthYearDto? = null,
	@SerialName("address")
	var address: AddressDto? = null,
	@SerialName("links")
	var links: SelfLinkDataDto? = null,
	@SerialName("name_elements")
	var nameElements: NameElementsDto? = null,
	@SerialName("natures_of_control")
	var naturesOfControl: List<String> = ArrayList(),
	var nationality: String? = null,
	@Keep var name: String? = null,
	var identification: IdentificationDto? = null,
)
