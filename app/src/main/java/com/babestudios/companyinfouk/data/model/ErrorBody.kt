package com.babestudios.companyinfouk.data.model

data class ErrorBody(
		val errors: List<Error>
)

data class Error(
		val error: String,
		val error_values: Map<String, String>?,
		val location: String?,
		val location_type: String?,
		val type: String
)