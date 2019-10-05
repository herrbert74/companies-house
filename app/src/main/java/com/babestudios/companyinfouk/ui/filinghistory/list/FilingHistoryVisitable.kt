package com.babestudios.companyinfouk.ui.filinghistory.list

import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import kotlinx.android.parcel.Parcelize

abstract class AbstractFilingHistoryVisitable {
	abstract fun type(filingHistoryTypeFactory: FilingHistoryAdapter.FilingHistoryTypeFactory): Int
}

@Parcelize
class FilingHistoryVisitable(val filingHistoryItem: FilingHistoryItem) : AbstractFilingHistoryVisitable(), Parcelable {
	override fun type(filingHistoryTypeFactory: FilingHistoryAdapter.FilingHistoryTypeFactory): Int {
		return filingHistoryTypeFactory.type(filingHistoryItem)
	}
}