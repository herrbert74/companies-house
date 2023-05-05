package com.babestudios.companyinfouk.data.model.charges

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassificationDto(
	@SerialName("description")
	var description: String? = null,
	@SerialName("type")
	var type: String? = null,
)
