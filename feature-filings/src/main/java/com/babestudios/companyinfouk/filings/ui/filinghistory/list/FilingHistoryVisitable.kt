package com.babestudios.companyinfouk.filings.ui.filinghistory.list

import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem

sealed class FilingHistoryVisitableBase {
	abstract fun type(filingHistoryTypeFactory: FilingHistoryAdapter.FilingHistoryTypeFactory): Int
}

class FilingHistoryVisitable(val filingHistoryItem: FilingHistoryItem) : FilingHistoryVisitableBase() {
	override fun type(filingHistoryTypeFactory: FilingHistoryAdapter.FilingHistoryTypeFactory): Int {
		return filingHistoryTypeFactory.type(filingHistoryItem)
	}
}
