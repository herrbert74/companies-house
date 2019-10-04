package com.babestudios.companyinfo.data.model.officers.appointments


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class FormerName(

		@SerializedName("forenames")
		var forenames: String? = null,
		@SerializedName("surname")
		var surname: String? = null

) : Parcelable
