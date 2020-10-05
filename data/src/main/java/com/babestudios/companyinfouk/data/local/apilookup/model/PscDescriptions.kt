package com.babestudios.companyinfouk.data.local.apilookup.model

import androidx.annotation.Keep

@Keep
data class PscDescriptions(
		val description: Map<String, String>,
		val short_description: Map<String, String>,
		val statement_description: Map<String, String>,
		val super_secure_description: Map<String, String>,
)
