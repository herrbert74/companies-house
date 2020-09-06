package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AnnualReturnDto(

		@SerializedName("last_made_up_to")
		var lastMadeUpTo: String? = null,

		@SerializedName("next_due")
		var nextDue: String? = null,

		var overdue: Boolean = false,

		@SerializedName("next_made_up_to")
		var nextMadeUpTo: String? = null

) : Parcelable
