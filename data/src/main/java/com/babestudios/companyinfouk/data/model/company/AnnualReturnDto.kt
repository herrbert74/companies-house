package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class AnnualReturnDto(

		@SerialName("last_made_up_to")
		var lastMadeUpTo: String? = null,

		@SerialName("next_due")
		var nextDue: String? = null,

		var overdue: Boolean = false,

		@SerialName("next_made_up_to")
		var nextMadeUpTo: String? = null

) : Parcelable
