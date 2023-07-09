package com.babestudios.companyinfouk.shared.data.model.filinghistory

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class FilingHistoryLinksDto(
	@SerialName("document_metadata")
	var documentMetadata: String? = null,
	@SerialName("self")
	var self: String? = null,
)
