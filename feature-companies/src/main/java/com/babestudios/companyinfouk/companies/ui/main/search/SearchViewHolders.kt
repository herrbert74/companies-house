package com.babestudios.companyinfouk.companies.ui.main.search

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_search_result.view.*

class SearchViewHolder(itemView: View) : BaseViewHolder<AbstractSearchVisitable>(itemView) {
	override fun bind(visitable: AbstractSearchVisitable) {
		val searchItem = (visitable as SearchVisitable).searchItem
		itemView.lblSearchResultsCompanyName?.text = searchItem.title
		itemView.lblSearchResultsAddress?.text = searchItem.addressSnippet
		itemView.lblSearchResultsActiveStatus?.text = searchItem.companyStatus
		itemView.lblSearchResultsIncorporated?.text = searchItem.description
	}
}