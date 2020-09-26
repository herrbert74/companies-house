package com.babestudios.companyinfouk.data.model.common


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class MonthYearDto(
		@SerializedName("year")
		var year: Int? = null,
		@SerializedName("month")
		var month: Int? = null
) : Parcelable
