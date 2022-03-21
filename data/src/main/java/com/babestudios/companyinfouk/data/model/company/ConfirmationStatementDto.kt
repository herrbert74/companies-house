package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class ConfirmationStatementDto(
		@SerializedName("last_made_up_to")
		var lastMadeUpTo: String? = null,

		@SerializedName("next_due")
		var nextDue: String? = null,

		@SerializedName("next_made_up_to")
		var nextMadeUpTo: String? = null,

		@SerializedName("overdue")
		var overdue: String? = null
) : Parcelable
