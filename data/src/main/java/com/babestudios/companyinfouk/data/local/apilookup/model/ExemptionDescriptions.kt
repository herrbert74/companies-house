package com.babestudios.companyinfouk.data.local.apilookup.model

import androidx.annotation.Keep

@Keep
@Suppress("ConstructorParameterNaming")
data class ExemptionDescriptions (
	val exemption_type: Map<String, String>,
	val exemption_description: Map<String, String>
)
