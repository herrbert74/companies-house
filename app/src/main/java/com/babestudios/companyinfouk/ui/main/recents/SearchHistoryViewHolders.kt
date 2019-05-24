package com.babestudios.companyinfouk.ui.main.recents

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_recent_searches.view.*
import kotlinx.android.synthetic.main.row_recent_searches_title.view.*

class SearchHistoryViewHolder(itemView: View) : BaseViewHolder<AbstractSearchHistoryVisitable>(itemView) {
	override fun bind(visitable: AbstractSearchHistoryVisitable) {
		val searchHistoryItem = (visitable as SearchHistoryVisitable).searchHistoryItem
		itemView.lblRecentSearchesCompanyName.text = searchHistoryItem.companyName
		itemView.lblRecentSearchesCompanyNumber.text = searchHistoryItem.companyNumber
	}
}

class SearchHistoryHeaderViewHolder(itemView: View) : BaseViewHolder<AbstractSearchHistoryVisitable>(itemView) {
	override fun bind(visitable: AbstractSearchHistoryVisitable) {
		val searchHistoryHeaderItem = (visitable as SearchHistoryHeaderVisitable).searchHistoryHeaderItem
		itemView.lblRecentSearchesTitle.text = searchHistoryHeaderItem.title
	}
}