package com.babestudios.companyinfouk.shared.data.model.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PreviousCompanyNameDto(

	@SerialName("ceased_on")
	var ceasedOn: String? = null,

	@SerialName("effective_from")
	var effectiveFrom: String? = null,

	@SerialName("name")
	var name: String? = null,

	)
