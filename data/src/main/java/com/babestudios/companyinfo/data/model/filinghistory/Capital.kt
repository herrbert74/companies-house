package com.babestudios.companyinfo.data.model.filinghistory


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Capital(
		@SerializedName("figure")
		var figure: String? = null,
		@SerializedName("currency")
		var currency: String? = null
) : Parcelable
