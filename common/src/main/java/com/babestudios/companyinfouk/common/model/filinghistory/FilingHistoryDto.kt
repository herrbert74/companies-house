package com.babestudios.companyinfouk.common.model.filinghistory


data class FilingHistoryDto(
	var startIndex: Int = 0,
	var itemsPerPage: Int = 0,
	var items: List<FilingHistoryItemDto> = emptyList(),
	var totalCount: Int = 0,
	var filingHistoryStatus: String = ""
)
