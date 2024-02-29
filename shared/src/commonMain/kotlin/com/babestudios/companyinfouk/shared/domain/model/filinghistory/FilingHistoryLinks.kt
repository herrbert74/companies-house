package com.babestudios.companyinfouk.shared.domain.model.filinghistory

import kotlinx.serialization.Serializable

@Serializable
data class FilingHistoryLinks(
		val documentMetadata: String = "",
		val self: String = "",
)
