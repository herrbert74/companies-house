package com.babestudios.companyinfouk.data.utils.errors.model

import androidx.annotation.Keep

@Keep
data class ErrorBody(
		val errors: List<ErrorEntity>? = null
) : ErrorEntity()

@Keep
open class ErrorEntity(
		val error: String? = null,
		val error_values: Map<String, String>? = null,
		val location: String? = null,
		val location_type: String? = null,
		val type: String? = null
)