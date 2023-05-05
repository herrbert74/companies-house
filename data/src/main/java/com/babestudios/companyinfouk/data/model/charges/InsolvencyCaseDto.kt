package com.babestudios.companyinfouk.data.model.charges

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InsolvencyCaseDto(
	@SerialName("case_number")
	var caseNumber: String? = null,
	@SerialName("links")
	var links: InsolvencyLinksDto? = null,
	@SerialName("transaction_id")
	var transactionId: String? = null,
)
