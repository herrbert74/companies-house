package com.babestudios.companyinfouk.ui.filinghistory

import android.view.View
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import net.medshr.android.base.mvp.lists.BaseViewHolder

/**
 *
 * Based on:
 * https://proandroiddev.com/writing-better-adapters-1b09758407d2
 * and:
 * https://github.com/dmitrikudrenko/BetterAdapters
 *
 */
class FilingHistoryTypesFactory : FilingHistoryAdapter.FilingHistoryTypesFactory {
	override fun type(filingHistoryItem: FilingHistoryItem): Int = R.layout.filing_history_list_item

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when(type) {
			R.layout.filing_history_list_item -> FilingHistoryViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}