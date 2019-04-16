package com.babestudios.companyinfouk.data.local.apilookup.model

data class ErrorMappings(
		val errors: Errors
)

data class Errors(
		val service: Map<String, String>
)