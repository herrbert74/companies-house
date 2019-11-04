package com.babestudios.companyinfouk.ui.main.recents

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_subtitle.view.*
import kotlinx.android.synthetic.main.row_two_lines.view.*

class SearchHistoryViewHolder(itemView: View) : BaseViewHolder<AbstractSearchHistoryVisitable>(itemView) {
	override fun bind(visitable: AbstractSearchHistoryVisitable) {
		val searchHistoryItem = (visitable as SearchHistoryVisitable).searchHistoryItem
		itemView.lblCommonTwoLinesTitle.text = searchHistoryItem.companyName
		itemView.lblCommonTwoLinesText.text = searchHistoryItem.companyNumber
	}
}

class SearchHistoryHeaderViewHolder(itemView: View) : BaseViewHolder<AbstractSearchHistoryVisitable>(itemView) {
	override fun bind(visitable: AbstractSearchHistoryVisitable) {
		val searchHistoryHeaderItem = (visitable as SearchHistoryHeaderVisitable).searchHistoryHeaderItem
		itemView.lblCommonSubtitle.text = searchHistoryHeaderItem.title
	}
}