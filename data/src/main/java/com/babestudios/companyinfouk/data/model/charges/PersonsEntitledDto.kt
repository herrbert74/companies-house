package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PersonsEntitledDto(
		@SerializedName("name")
		var name: String? = null
) : Parcelable
