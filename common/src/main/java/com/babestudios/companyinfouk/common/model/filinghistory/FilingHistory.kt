package com.babestudios.companyinfouk.common.model.filinghistory


data class FilingHistory(
		var startIndex: Int = 0,
		var itemsPerPage: Int = 0,
		var items: List<FilingHistoryItem> = emptyList(),
		var totalCount: Int = 0,
		var filingHistoryStatus: String = "",
)
