package com.babestudios.companyinfouk.data.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MonthYearDto(
	@SerialName("year")
	var year: Int? = null,
	@SerialName("month")
	var month: Int? = null,
)
