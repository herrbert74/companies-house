package com.babestudios.companyinfouk.filings.ui

import android.net.Uri
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.common.model.filinghistory.Category
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryVisitable
import kotlinx.android.parcel.RawValue
import okhttp3.ResponseBody

data class FilingsState(
		//Filings
		val filingsRequest: Async<FilingHistory> = Uninitialized,
		val filingsHistory: List<FilingHistoryVisitable> = emptyList(),
		val totalFilingsCount: Int = 0,
		@PersistState
		val companyNumber: String = "",
		val filingCategoryFilter: Category = Category.CATEGORY_SHOW_ALL,

		//Filing details
		@PersistState
		val filingHistoryItem: FilingHistoryItem = FilingHistoryItem(),
		val documentRequest: Async<ResponseBody> = Uninitialized,
		var pdfResponseBody : ResponseBody? = null,
		val writeDocumentRequest: Async<Uri> = Uninitialized,
		val pdfUri: @RawValue Uri? = null
) : MvRxState
