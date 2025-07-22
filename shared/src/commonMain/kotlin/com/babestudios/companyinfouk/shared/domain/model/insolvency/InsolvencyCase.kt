package com.babestudios.companyinfouk.shared.domain.model.insolvency

import kotlinx.serialization.Serializable

@Serializable
class InsolvencyCase(
	var dates: List<Date> = ArrayList(),
	var practitioners: List<Practitioner> = ArrayList(),
	var type: String? = null,
)
