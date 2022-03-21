package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InsolvencyCaseDto(
		@SerializedName("case_number")
		var caseNumber: String? = null,
		@SerializedName("links")
		var links: InsolvencyLinksDto? = null,
		@SerializedName("transaction_id")
		var transactionId: String? = null
) : Parcelable
