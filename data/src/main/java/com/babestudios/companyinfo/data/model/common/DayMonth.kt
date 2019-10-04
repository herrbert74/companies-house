package com.babestudios.companyinfo.data.model.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class DayMonth(

		@SerializedName("day")
		var day: String? = null,

		@SerializedName("month")
		var month: String? = null

) : Parcelable