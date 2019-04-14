package com.babestudios.companyinfouk.data.local.apilookup.model

data class Error(
		val errors: Errors
)

data class Errors(
		val service: Map<String, String>
)