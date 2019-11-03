package com.babestudios.companyinfouk.filings.ui

import android.net.Uri
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.common.model.filinghistory.CategoryDto
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryItemDto
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryVisitable
import kotlinx.android.parcel.RawValue
import okhttp3.ResponseBody

data class FilingsState(
		//Filings
		val filingsRequest: Async<FilingHistoryDto> = Uninitialized,
		val filingsHistory: List<FilingHistoryVisitable> = emptyList(),
		val totalFilingsCount: Int = 0,
		val companyNumber: String = "",
		val filingCategoryFilter: CategoryDto = CategoryDto.CATEGORY_SHOW_ALL,

		//Filing details
		val filingHistoryItem: FilingHistoryItemDto = FilingHistoryItemDto(),
		val documentRequest: Async<ResponseBody> = Uninitialized,
		var pdfResponseBody : ResponseBody? = null,
		val writeDocumentRequest: Async<Uri> = Uninitialized,
		val pdfUri: @RawValue Uri? = null
) : MvRxState