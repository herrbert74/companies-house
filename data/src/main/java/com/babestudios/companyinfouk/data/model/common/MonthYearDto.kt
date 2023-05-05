package com.babestudios.companyinfouk.data.model.common


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class MonthYearDto(
		@SerialName("year")
		var year: Int? = null,
		@SerialName("month")
		var month: Int? = null
) : Parcelable
