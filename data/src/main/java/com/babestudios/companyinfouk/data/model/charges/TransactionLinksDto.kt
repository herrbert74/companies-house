package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionLinksDto(
		@SerializedName("filing")
		var filing: String? = null,
		@SerializedName("insolvency_case")
		var insolvencyCase: String? = null
) : Parcelable
