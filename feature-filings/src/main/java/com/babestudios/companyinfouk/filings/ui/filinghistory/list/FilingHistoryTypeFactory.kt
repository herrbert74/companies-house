package com.babestudios.companyinfouk.filings.ui.filinghistory.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryItemDto
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
	override fun type(filingHistoryItem: FilingHistoryItemDto): Int = R.layout.row_filing_history

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when(type) {
			R.layout.row_filing_history -> FilingHistoryViewHolder(view)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
