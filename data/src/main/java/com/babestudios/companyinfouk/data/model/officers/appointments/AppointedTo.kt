package com.babestudios.companyinfouk.data.model.officers.appointments


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AppointedTo(

		@SerializedName("company_name")
		var companyName: String? = null,
		@SerializedName("company_number")
		var companyNumber: String? = null,
		@SerializedName("company_status")
		var companyStatus: String? = null

) : Parcelable
