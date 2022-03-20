package com.babestudios.companyinfouk.companies.ui.main.search

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResultItem

class SearchTypeFactory : SearchAdapter.SearchTypeFactory {
	override fun type(searchItem: CompanySearchResultItem): Int = R.layout.row_search_result

	override fun holder(type: Int, binding: ViewBinding): BaseViewHolder<SearchVisitableBase> {
		return when (type) {
			R.layout.row_search_result -> SearchViewHolder(binding)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
