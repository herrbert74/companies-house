package com.babestudios.companyinfouk.shared.data.model.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MustFileWithinDto(

	@SerialName("months")
	var months: String? = null,

	)
