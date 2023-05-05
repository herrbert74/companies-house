package com.babestudios.companyinfouk.data.model.insolvency

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InsolvencyCaseLinksDto(
	@SerialName("charge")
	var charge: String? = null,
)
