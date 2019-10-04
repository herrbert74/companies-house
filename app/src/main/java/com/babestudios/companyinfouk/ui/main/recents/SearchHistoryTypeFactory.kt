package com.babestudios.companyinfouk.ui.main.recents

import android.view.View
import com.babestudios.companyinfouk.R
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfo.data.model.search.SearchHistoryItem

class SearchHistoryTypeFactory : SearchHistoryAdapter.SearchHistoryTypeFactory {
	override fun type(searchHistoryItem: SearchHistoryItem): Int = R.layout.row_recent_searches
	override fun type(searchHistoryHeaderItem: SearchHistoryHeaderItem): Int = R.layout.row_recent_searches_title

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_recent_searches -> SearchHistoryViewHolder(view)
			R.layout.row_recent_searches_title -> SearchHistoryHeaderViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}