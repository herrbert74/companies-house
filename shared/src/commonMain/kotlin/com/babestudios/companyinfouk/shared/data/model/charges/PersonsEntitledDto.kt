package com.babestudios.companyinfouk.shared.data.model.charges

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonsEntitledDto(
	@SerialName("name")
	var name: String? = null,
)
