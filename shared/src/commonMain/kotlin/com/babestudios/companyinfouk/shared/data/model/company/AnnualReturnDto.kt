package com.babestudios.companyinfouk.shared.data.model.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AnnualReturnDto(

	@SerialName("last_made_up_to")
	var lastMadeUpTo: String? = null,

	@SerialName("next_due")
	var nextDue: String? = null,

	var overdue: Boolean = false,

	@SerialName("next_made_up_to")
	var nextMadeUpTo: String? = null,

	)
