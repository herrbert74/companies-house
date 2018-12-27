package com.babestudios.companyinfouk.ui.filinghistory

import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem

abstract class AbstractFilingHistoryVisitable {
	abstract fun type(filingHistoryTypesFactory: FilingHistoryAdapter.FilingHistoryTypesFactory): Int
}

class FilingHistoryVisitable(val filingHistoryItem: FilingHistoryItem) : AbstractFilingHistoryVisitable() {
	override fun type(filingHistoryTypesFactory: FilingHistoryAdapter.FilingHistoryTypesFactory): Int {
		return filingHistoryTypesFactory.type(filingHistoryItem)
	}
}