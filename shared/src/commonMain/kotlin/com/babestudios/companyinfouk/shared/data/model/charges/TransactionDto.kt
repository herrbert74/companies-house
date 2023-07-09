package com.babestudios.companyinfouk.shared.data.model.charges

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
	@SerialName("delivered_on")
	var deliveredOn: String? = null,
	@SerialName("filing_type")
	var filingType: String? = null,
	@SerialName("insolvency_case_number")
	var insolvencyCaseNumber: String? = null,
	@SerialName("links")
	var links: TransactionLinksDto? = null,
	@SerialName("transaction_id")
	var transactionId: String? = null,
)
