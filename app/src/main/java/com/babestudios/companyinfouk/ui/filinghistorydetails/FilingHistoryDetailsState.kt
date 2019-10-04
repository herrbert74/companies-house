package com.babestudios.companyinfouk.ui.filinghistorydetails

import android.net.Uri
import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfo.data.model.filinghistory.FilingHistoryItem
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import okhttp3.ResponseBody

enum class ContentChange {
	NONE,
	FILING_HISTORY_ITEM_RECEIVED,
	PDF_RECEIVED,
	PDF_WRITTEN
}

@Parcelize
data class FilingHistoryDetailsState(
		var filingHistoryItem: FilingHistoryItem? = null,
		var filingHistoryItemDescription: String? = null,
		var contentChange: ContentChange = ContentChange.NONE,
		//pdfResponseBody: @RawValue ResponseBody? = null,
		var pdfUri: @RawValue Uri? = null
) : BaseState(), Parcelable {
	@IgnoredOnParcel
	var pdfResponseBody : ResponseBody? = null
}