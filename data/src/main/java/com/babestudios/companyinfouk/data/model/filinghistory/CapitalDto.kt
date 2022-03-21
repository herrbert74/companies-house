package com.babestudios.companyinfouk.data.model.filinghistory


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class CapitalDto(
		@SerializedName("figure")
		var figure: String? = null,
		@SerializedName("currency")
		var currency: String? = null
) : Parcelable
