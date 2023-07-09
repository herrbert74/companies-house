package com.babestudios.companyinfouk.shared.domain.model.enumerations

@Suppress("ConstructorParameterNaming")
data class PscDescriptions(
		val kind: Map<String, String>,
		val description: Map<String, String>,
		val short_description: Map<String, String>,
		val statement_description: Map<String, String>,
		val super_secure_description: Map<String, String>,
)
