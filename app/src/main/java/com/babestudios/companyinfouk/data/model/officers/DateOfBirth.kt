package com.babestudios.companyinfouk.data.model.officers


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class DateOfBirth(
		@SerializedName("year")
		var year: Long? = null,
		@SerializedName("month")
		var month: Long? = null
) : Parcelable
