package com.babestudios.companyinfouk.shared.data.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SelfLinkDataDto(
	@SerialName("self")
	val self: String,
)
