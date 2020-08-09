package com.babestudios.companyinfouk.filings.ui.filinghistory.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.R

/**
 *
 * Based on:
 * https://proandroiddev.com/writing-better-adapters-1b09758407d2
 * and:
 * https://github.com/dmitrikudrenko/BetterAdapters
 *
 */
class FilingHistoryTypeFactory : FilingHistoryAdapter.FilingHistoryTypeFactory {
	override fun type(filingHistoryItem: FilingHistoryItem): Int = R.layout.row_filing_history

	override fun holder(type: Int, binding: ViewBinding): BaseViewHolder<FilingHistoryVisitableBase> {
		return when(type) {
			R.layout.row_filing_history -> FilingHistoryViewHolder(binding)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
