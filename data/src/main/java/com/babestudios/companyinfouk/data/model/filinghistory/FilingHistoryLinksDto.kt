package com.babestudios.companyinfouk.data.model.filinghistory

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class FilingHistoryLinksDto(
	@SerialName("document_metadata")
	var documentMetadata: String? = null,
	@SerialName("self")
	var self: String? = null,
) : Parcelable
