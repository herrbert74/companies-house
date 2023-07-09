package com.babestudios.companyinfouk.shared.data.model.insolvency

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InsolvencyCaseLinksDto(
	@SerialName("charge")
	var charge: String? = null,
)
