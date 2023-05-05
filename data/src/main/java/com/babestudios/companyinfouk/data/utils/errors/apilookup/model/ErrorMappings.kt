package com.babestudios.companyinfouk.data.utils.errors.apilookup.model

import androidx.annotation.Keep

@Keep
data class ErrorMappings(
	val errors: Errors,
)

@Keep
data class Errors(
	val service: Map<String, String>,
)
