package com.babestudios.companyinfouk.data.model.persons

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Identification(
		@SerializedName("country_registered")
		var countryRegistered: String? = null,
		@SerializedName("legal_authority")
		var legalAuthority: String? = null,
		@SerializedName("legal_form")
		var LegalForm: String? = null,
		@SerializedName("place_registered")
		var placeRegistered: String? = null,
		@SerializedName("registration_number")
		var registrationNumber: String? = null
) : Parcelable
