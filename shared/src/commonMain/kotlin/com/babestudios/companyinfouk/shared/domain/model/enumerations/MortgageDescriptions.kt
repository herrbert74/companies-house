package com.babestudios.companyinfouk.shared.domain.model.enumerations

@Suppress("ConstructorParameterNaming")
data class MortgageDescriptions(
		val classificationDesc: Map<String, String>,
		val status: Map<String, String>,
		val assets_ceased_released: Map<String, String>,
		val particular_flags: Map<String, String>,
		val secured_details_description: Map<String, String>,
		val alterations_description: Map<String, String>,
		val filing_type: Map<String, String>,
)
