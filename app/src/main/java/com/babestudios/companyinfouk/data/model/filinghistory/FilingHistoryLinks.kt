package com.babestudios.companyinfouk.data.model.filinghistory

import com.google.gson.annotations.SerializedName

class FilingHistoryLinks {
	@SerializedName("document_metadata")
	var documentMetadata: String? = null
	@SerializedName("self")
	var self: String? = null
}
