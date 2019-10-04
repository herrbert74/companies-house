package com.babestudios.companyinfo.data.model.officers


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Identification(
		@SerializedName("place_registered")
		var placeRegistered: String? = null,
		@SerializedName("identification_type")
		var identificationType: String? = null,
		@SerializedName("registration_number")
		var registrationNumber: String? = null
) : Parcelable
