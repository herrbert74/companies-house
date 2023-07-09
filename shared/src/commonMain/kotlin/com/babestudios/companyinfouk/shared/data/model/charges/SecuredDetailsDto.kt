package com.babestudios.companyinfouk.shared.data.model.charges

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SecuredDetailsDto(
	@SerialName("description")
	var description: String? = null,
	@SerialName("type")
	var type: String? = null,
)
