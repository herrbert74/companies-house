package com.babestudios.companyinfouk.data.model.officers.appointments

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AppointmentLinks(

		@SerializedName("company")
		var company: String? = null

) : Parcelable
