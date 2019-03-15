package com.babestudios.companyinfouk.data.model.persons


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Links2(
		@SerializedName("self")
		var self: String? = null
) : Parcelable
