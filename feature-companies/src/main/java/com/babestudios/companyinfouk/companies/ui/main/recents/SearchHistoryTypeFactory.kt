package com.babestudios.companyinfouk.companies.ui.main.recents

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem

class SearchHistoryTypeFactory : SearchHistoryAdapter.SearchHistoryTypeFactory {
	override fun type(searchHistoryItem: SearchHistoryItem): Int = R.layout.row_two_lines
	override fun type(searchHistoryHeaderItem: SearchHistoryHeaderItem): Int = R.layout.row_subtitle

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_two_lines -> SearchHistoryViewHolder(view)
			R.layout.row_subtitle -> SearchHistoryHeaderViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}