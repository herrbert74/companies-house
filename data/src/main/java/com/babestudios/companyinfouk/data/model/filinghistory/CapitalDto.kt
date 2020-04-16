package com.babestudios.companyinfouk.data.model.filinghistory


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CapitalDto(
		@SerializedName("figure")
		var figure: String? = null,
		@SerializedName("currency")
		var currency: String? = null
) : Parcelable
