package com.babestudios.companyinfouk.shared.data.model.charges

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScottishAlterationsDto(
	@SerialName("has_alterations_to_order")
	var hasAlterationsToOrder: String? = null,
	@SerialName("has_alterations_to_prohibitions")
	var hasAlterationsToProhibitions: String? = null,
	@SerialName("has_restricting_provisions")
	var hasRestrictingProvisions: String? = null,
)
