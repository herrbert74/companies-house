package com.babestudios.companyinfouk.shared.data.model.insolvency

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DateDto(
	@SerialName("date")
	var date: String? = null,
	@SerialName("type")
	var type: String? = null,
)
