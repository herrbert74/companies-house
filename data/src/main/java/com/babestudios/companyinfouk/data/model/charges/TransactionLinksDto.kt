package com.babestudios.companyinfouk.data.model.charges

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionLinksDto(
	@SerialName("filing")
	var filing: String? = null,
	@SerialName("insolvency_case")
	var insolvencyCase: String? = null,
)
