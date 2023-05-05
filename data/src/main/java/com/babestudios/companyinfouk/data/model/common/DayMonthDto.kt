package com.babestudios.companyinfouk.data.model.common

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class DayMonthDto(

		@SerialName("day")
		var day: String? = null,

		@SerialName("month")
		var month: String? = null

) : Parcelable
