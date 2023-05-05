package com.babestudios.companyinfouk.data.model.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LastAccountsDto(
	@SerialName("made_up_to")
	var madeUpTo: String? = null,

	@SerialName("type")
	var type: String? = null,

	@SerialName("period_start_on")
	var periodStartOn: String? = null,

	@SerialName("period_end_on")
	var periodEndOn: String? = null,
)

