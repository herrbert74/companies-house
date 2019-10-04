package com.babestudios.companyinfo.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class MustFileWithin (

	@SerializedName("months")
	var months: String? = null

):Parcelable