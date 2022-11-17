package com.babestudios.companyinfouk.data.local.apilookup.model

import androidx.annotation.Keep

@Keep
@Suppress("ConstructorParameterNaming")
data class DisqualifiedOfficersDescriptions(
		val description_identifier: Map<String, String>,
		val act: Map<String, String>,
		val disqualification_type: Map<String, String>
)
