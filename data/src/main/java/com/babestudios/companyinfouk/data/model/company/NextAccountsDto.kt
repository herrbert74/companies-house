package com.babestudios.companyinfouk.data.model.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class NextAccountsDto(
	@SerialName("due_on")
	var dueOn: String? = null,

	var overdue: Boolean? = null,

	@SerialName("period_start_on")
	var periodStartOn: String? = null,

	@SerialName("period_end_on")
	var periodEndOn: String? = null,
)
