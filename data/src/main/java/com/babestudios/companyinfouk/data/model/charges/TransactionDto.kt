package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionDto(
		@SerializedName("delivered_on")
	var deliveredOn: String? = null,
		@SerializedName("filing_type")
	var filingType: String? = null,
		@SerializedName("insolvency_case_number")
	var insolvencyCaseNumber: String? = null,
		@SerializedName("links")
	var links: TransactionLinksDto? = null,
		@SerializedName("transaction_id")
	var transactionId: String? = null
	): Parcelable
