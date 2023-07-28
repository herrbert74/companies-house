package com.babestudios.companyinfouk.shared.data.error

data class ErrorBody(val errors: List<ErrorEntity>? = null) : ErrorEntity()

@Suppress("ConstructorParameterNaming")
open class ErrorEntity(
	val error: String? = null,
	val error_values: Map<String, String>? = null,
	val location: String? = null,
	val location_type: String? = null,
	val type: String? = null
)
