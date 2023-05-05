package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class InsolvencyCaseDto(
		@SerialName("case_number")
		var caseNumber: String? = null,
		@SerialName("links")
		var links: InsolvencyLinksDto? = null,
		@SerialName("transaction_id")
		var transactionId: String? = null
) : Parcelable
