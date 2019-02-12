package com.babestudios.companyinfouk.ui.filinghistory.list

import android.view.View
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.babestudios.base.mvp.list.BaseViewHolder

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

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when(type) {
			R.layout.row_filing_history -> FilingHistoryViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}