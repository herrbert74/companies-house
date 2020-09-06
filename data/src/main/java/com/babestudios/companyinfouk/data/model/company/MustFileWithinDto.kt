package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class MustFileWithinDto (

	@SerializedName("months")
	var months: String? = null

):Parcelable
