package com.babestudios.companyinfouk.companies.ui.main.recents

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import java.lang.IllegalStateException

class SearchHistoryTypeFactory : SearchHistoryAdapter.SearchHistoryTypeFactory {
	override fun type(searchHistoryItem: SearchHistoryItem): Int = R.layout.row_two_lines
	override fun type(searchHistoryHeaderItem: SearchHistoryHeaderItem): Int = R.layout.row_subtitle

	override fun holder(type: Int, binding: ViewBinding): BaseViewHolder<SearchHistoryVisitableBase> {
		return when (type) {
			R.layout.row_two_lines -> SearchHistoryViewHolder(binding)
			R.layout.row_subtitle -> SearchHistoryHeaderViewHolder(binding)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
