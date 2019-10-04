package com.babestudios.companyinfo.data.model.common


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class MonthYear(
		@SerializedName("year")
		var year: Long? = null,
		@SerializedName("month")
		var month: Long? = null
) : Parcelable
