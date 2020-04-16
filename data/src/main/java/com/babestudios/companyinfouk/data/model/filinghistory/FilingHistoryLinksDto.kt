package com.babestudios.companyinfouk.data.model.filinghistory

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class FilingHistoryLinksDto(
		@SerializedName("document_metadata")
		var documentMetadata: String? = null,
		@SerializedName("self")
		var self: String? = null
) : Parcelable
