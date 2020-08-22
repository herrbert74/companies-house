package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClassificationDto(
	@SerializedName("description")
	var description: String? = null,
	@SerializedName("type")
	var type: String? = null
) : Parcelable
