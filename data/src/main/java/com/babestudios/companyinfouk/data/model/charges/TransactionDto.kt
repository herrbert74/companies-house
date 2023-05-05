package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
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
	var transactionId: String? = null
	): Parcelable
