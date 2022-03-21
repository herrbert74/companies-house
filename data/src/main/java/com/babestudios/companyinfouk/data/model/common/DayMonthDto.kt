package com.babestudios.companyinfouk.data.model.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class DayMonthDto(

		@SerializedName("day")
		var day: String? = null,

		@SerializedName("month")
		var month: String? = null

) : Parcelable
