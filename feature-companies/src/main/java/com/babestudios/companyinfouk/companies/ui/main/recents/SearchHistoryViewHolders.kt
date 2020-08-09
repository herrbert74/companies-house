package com.babestudios.companyinfouk.companies.ui.main.recents

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.common.databinding.RowSubtitleBinding
import com.babestudios.companyinfouk.common.databinding.RowTwoLinesBinding

class SearchHistoryViewHolder(_binding: ViewBinding)
	: BaseViewHolder<SearchHistoryVisitableBase>(_binding) {
	override fun bind(visitable: SearchHistoryVisitableBase) {
		val binding = _binding as RowTwoLinesBinding
		val searchHistoryItem = (visitable as SearchHistoryVisitable).searchHistoryItem
		binding.lblCommonTwoLinesTitle.text = searchHistoryItem.companyName
		binding.lblCommonTwoLinesText.text = searchHistoryItem.companyNumber
	}
}

class SearchHistoryHeaderViewHolder(_binding: ViewBinding)
	: BaseViewHolder<SearchHistoryVisitableBase>(_binding) {
	override fun bind(visitable: SearchHistoryVisitableBase) {
		val binding = _binding as RowSubtitleBinding
		val searchHistoryHeaderItem =
				(visitable as SearchHistoryHeaderVisitable).searchHistoryHeaderItem
		binding.lblCommonSubtitle.text = searchHistoryHeaderItem.title
	}
}
