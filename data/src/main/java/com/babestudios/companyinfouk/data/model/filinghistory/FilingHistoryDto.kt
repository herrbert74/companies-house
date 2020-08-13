package com.babestudios.companyinfouk.data.model.filinghistory


import com.google.gson.annotations.SerializedName
import java.util.*

class FilingHistoryDto(
		@SerializedName("start_index")
		val startIndex: Int? = null,

		@SerializedName("items_per_page")
		val itemsPerPage: Int? = null,

		@SerializedName("items")
		val items: List<FilingHistoryItemDto> = ArrayList(),

		@SerializedName("total_count")
		val totalCount: Int? = null,

		@SerializedName("filing_history_status")
		val filingHistoryStatus: String? = null
)
