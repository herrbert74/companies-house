package com.babestudios.companyinfouk.filings.ui.filinghistory.list

import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryItem

@Suppress("UnnecessaryAbstractClass")
abstract class AbstractFilingHistoryVisitable {
	abstract fun type(filingHistoryTypeFactory: FilingHistoryAdapter.FilingHistoryTypeFactory): Int
}

class FilingHistoryVisitable(val filingHistoryItem: FilingHistoryItem) : AbstractFilingHistoryVisitable() {
	override fun type(filingHistoryTypeFactory: FilingHistoryAdapter.FilingHistoryTypeFactory): Int {
		return filingHistoryTypeFactory.type(filingHistoryItem)
	}
}
