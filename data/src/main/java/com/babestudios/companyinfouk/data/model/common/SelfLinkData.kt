package com.babestudios.companyinfouk.data.model.common


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelfLinkData(
		@SerializedName("self")
		val self: String
) : Parcelable