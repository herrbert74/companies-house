package com.babestudios.companyinfouk.data.model.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class OriginatingRegistryDto(

	@SerialName("country")
	var country: String? = null,

	@SerialName("name")
	var name: String? = null,

	)
