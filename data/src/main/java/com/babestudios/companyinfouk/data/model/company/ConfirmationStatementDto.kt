package com.babestudios.companyinfouk.data.model.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ConfirmationStatementDto(
	@SerialName("last_made_up_to")
	var lastMadeUpTo: String? = null,

	@SerialName("next_due")
	var nextDue: String? = null,

	@SerialName("next_made_up_to")
	var nextMadeUpTo: String? = null,

	@SerialName("overdue")
	var overdue: Boolean? = null,
)
