package com.babestudios.companyinfouk.companies.ui.main.recents

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.common.databinding.RowSubtitleBinding
import com.babestudios.companyinfouk.common.databinding.RowTwoLinesBinding

class SearchHistoryViewHolder(
	private val rawBinding: ViewBinding
) : BaseViewHolder<SearchHistoryVisitableBase>(rawBinding) {

	override fun bind(visitable: SearchHistoryVisitableBase) {
		val binding = rawBinding as RowTwoLinesBinding
		val searchHistoryItem = (visitable as SearchHistoryVisitable).searchHistoryItem
		binding.lblCommonTwoLinesTitle.text = searchHistoryItem.companyName
		binding.lblCommonTwoLinesText.text = searchHistoryItem.companyNumber
	}

}

class SearchHistoryHeaderViewHolder(
	private val rawBinding: ViewBinding
) : BaseViewHolder<SearchHistoryVisitableBase>(rawBinding) {

	override fun bind(visitable: SearchHistoryVisitableBase) {
		val binding = rawBinding as RowSubtitleBinding
		val searchHistoryHeaderItem = (visitable as SearchHistoryHeaderVisitable).searchHistoryHeaderItem
		binding.lblCommonSubtitle.text = searchHistoryHeaderItem.title
	}

}
