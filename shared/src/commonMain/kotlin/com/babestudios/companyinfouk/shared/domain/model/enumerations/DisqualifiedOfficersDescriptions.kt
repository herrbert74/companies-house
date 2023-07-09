package com.babestudios.companyinfouk.shared.domain.model.enumerations

@Suppress("ConstructorParameterNaming")
data class DisqualifiedOfficersDescriptions(
	val description_identifier: Map<String, String>,
	val act: Map<String, String>,
	val disqualification_type: Map<String, String>,
)
