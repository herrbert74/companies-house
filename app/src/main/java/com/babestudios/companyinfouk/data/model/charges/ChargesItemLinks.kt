package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargesItemLinks (
	@SerializedName("self")
	var self: String? = null
) : Parcelable
