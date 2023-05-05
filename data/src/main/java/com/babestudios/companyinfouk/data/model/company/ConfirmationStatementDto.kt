package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class ConfirmationStatementDto(
		@SerialName("last_made_up_to")
		var lastMadeUpTo: String? = null,

		@SerialName("next_due")
		var nextDue: String? = null,

		@SerialName("next_made_up_to")
		var nextMadeUpTo: String? = null,

		@SerialName("overdue")
		var overdue: Boolean? = null
) : Parcelable
