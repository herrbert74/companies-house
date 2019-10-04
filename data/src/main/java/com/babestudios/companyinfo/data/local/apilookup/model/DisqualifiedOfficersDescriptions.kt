package com.babestudios.companyinfo.data.local.apilookup.model

import androidx.annotation.Keep

@Keep
data class DisqualifiedOfficersDescriptions(
		val description_identifier: Map<String, String>,
		val act: Map<String, String>,
		val disqualification_type: Map<String, String>
)