package com.babestudios.companyinfouk.data.model.filinghistory

import kotlinx.serialization.SerialName
import java.util.*
import kotlinx.serialization.Serializable

@Serializable
class FilingHistoryDto(
	@SerialName("start_index")
	val startIndex: Int? = null,

	@SerialName("items_per_page")
	val itemsPerPage: Int? = null,

	@SerialName("items")
	val items: List<FilingHistoryItemDto> = ArrayList(),

	@SerialName("total_count")
	val totalCount: Int? = null,

	@SerialName("filing_history_status")
	val filingHistoryStatus: String? = null,
)
