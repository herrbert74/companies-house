package com.babestudios.companyinfouk.ui.main.search

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_search_result.view.*

class SearchViewHolder(itemView: View) : BaseViewHolder<AbstractSearchVisitable>(itemView) {
	override fun bind(visitable: AbstractSearchVisitable) {
		val searchItem = (visitable as SearchVisitable).searchItem
		itemView.lblCompanyName?.text = searchItem.title
		itemView.lblAddress?.text = searchItem.addressSnippet
		itemView.lblActiveStatus?.text = searchItem.companyStatus
		itemView.lblIncorporated?.text = searchItem.description
	}
}