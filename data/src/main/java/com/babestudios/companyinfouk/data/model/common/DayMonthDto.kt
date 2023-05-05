package com.babestudios.companyinfouk.data.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DayMonthDto(

	@SerialName("day")
	var day: String? = null,

	@SerialName("month")
	var month: String? = null,

	)
