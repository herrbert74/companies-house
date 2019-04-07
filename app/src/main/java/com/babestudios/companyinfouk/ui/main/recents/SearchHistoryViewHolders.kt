package com.babestudios.companyinfouk.ui.main.recents

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_recent_searches.view.*
import kotlinx.android.synthetic.main.row_recent_searches_title.view.*

class SearchHistoryViewHolder(itemView: View) : BaseViewHolder<AbstractSearchHistoryVisitable>(itemView) {
	override fun bind(visitable: AbstractSearchHistoryVisitable) {
		val searchHistoryItem = (visitable as SearchHistoryVisitable).searchHistoryItem
		itemView.lblCompanyName.text = searchHistoryItem.companyName
		itemView.lblCompanyNumber.text = searchHistoryItem.companyNumber
	}
}

class SearchHistoryHeaderViewHolder(itemView: View) : BaseViewHolder<AbstractSearchHistoryVisitable>(itemView) {
	override fun bind(visitable: AbstractSearchHistoryVisitable) {
		val searchHistoryHeaderItem = (visitable as SearchHistoryHeaderVisitable).searchHistoryHeaderItem
		itemView.lblTitle.text = searchHistoryHeaderItem.title
	}
}