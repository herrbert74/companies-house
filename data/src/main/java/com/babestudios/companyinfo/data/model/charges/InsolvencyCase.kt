package com.babestudios.companyinfo.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InsolvencyCase(
		@SerializedName("case_number")
		var caseNumber: String? = null,
		@SerializedName("links")
		var links: InsolvencyLinks? = null,
		@SerializedName("transaction_id")
		var transactionId: String? = null
) : Parcelable
