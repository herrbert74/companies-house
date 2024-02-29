package com.babestudios.companyinfouk.shared.domain.model.insolvency

import kotlinx.serialization.Serializable

@Serializable
data class Date (
	var date: String,
	var type: String,
)
