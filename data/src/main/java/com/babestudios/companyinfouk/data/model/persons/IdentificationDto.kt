package com.babestudios.companyinfouk.data.model.persons

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class IdentificationDto(
	@SerialName("country_registered")
	var countryRegistered: String? = null,
	@SerialName("legal_authority")
	var legalAuthority: String? = null,
	@SerialName("legal_form")
	var legalForm: String? = null,
	@SerialName("place_registered")
	var placeRegistered: String? = null,
	@SerialName("registration_number")
	var registrationNumber: String? = null,
)
