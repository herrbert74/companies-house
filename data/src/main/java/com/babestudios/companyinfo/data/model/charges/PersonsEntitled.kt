package com.babestudios.companyinfo.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PersonsEntitled(
		@SerializedName("name")
		var name: String? = null
) : Parcelable
