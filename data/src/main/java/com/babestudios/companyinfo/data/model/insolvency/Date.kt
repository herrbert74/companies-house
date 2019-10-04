package com.babestudios.companyinfo.data.model.insolvency


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Date (
	@SerializedName("date")
	var date: String? = null,
	@SerializedName("type")
	var type: String? = null
): Parcelable
