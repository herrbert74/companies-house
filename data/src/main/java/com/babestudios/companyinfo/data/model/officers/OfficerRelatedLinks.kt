package com.babestudios.companyinfo.data.model.officers


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class OfficerRelatedLinks(
		@SerializedName("appointments")
		var appointments: String? = null
) : Parcelable
