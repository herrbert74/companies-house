package com.babestudios.companyinfouk.companies.ui.main.recents

import android.view.View
import android.widget.TextView
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.companies.R

class SearchHistoryViewHolder(itemView: View) : BaseViewHolder<AbstractSearchHistoryVisitable>(itemView) {
	override fun bind(visitable: AbstractSearchHistoryVisitable) {
		val searchHistoryItem = (visitable as SearchHistoryVisitable).searchHistoryItem
		val lblCommonTwoLinesTitle = itemView.findViewById<TextView>(R.id.lblCommonTwoLinesTitle)
		lblCommonTwoLinesTitle.text = searchHistoryItem.companyName
		val lblCommonTwoLinesText = itemView.findViewById<TextView>(R.id.lblCommonTwoLinesText)
		lblCommonTwoLinesText.text = searchHistoryItem.companyNumber
	}
}

class SearchHistoryHeaderViewHolder(itemView: View) : BaseViewHolder<AbstractSearchHistoryVisitable>(itemView) {
	override fun bind(visitable: AbstractSearchHistoryVisitable) {
		val searchHistoryHeaderItem = (visitable as SearchHistoryHeaderVisitable).searchHistoryHeaderItem
		val lblCommonSubtitle = itemView.findViewById<TextView>(R.id.lblCommonSubtitle)
		lblCommonSubtitle.text = searchHistoryHeaderItem.title
	}
}