package com.babestudios.companyinfouk.data.model.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Links(

		@SerializedName("self")
		var self: String? = null

) : Parcelable