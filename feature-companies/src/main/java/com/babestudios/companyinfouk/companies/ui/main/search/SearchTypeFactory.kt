package com.babestudios.companyinfouk.companies.ui.main.search

import android.view.View
import com.babestudios.companyinfouk.R
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.search.CompanySearchResultItem

class SearchTypeFactory : SearchAdapter.SearchTypeFactory {
	override fun type(searchItem: CompanySearchResultItem): Int = R.layout.row_search_result

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_search_result -> SearchViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}