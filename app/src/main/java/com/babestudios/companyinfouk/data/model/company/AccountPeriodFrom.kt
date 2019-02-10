package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AccountPeriodFrom (
	@SerializedName("day")
	var day: String? = null,
	@SerializedName("month")
	var month: String? = null
): Parcelable
