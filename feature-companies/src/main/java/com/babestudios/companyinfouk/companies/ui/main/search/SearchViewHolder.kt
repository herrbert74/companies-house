package com.babestudios.companyinfouk.companies.ui.main.search

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.companies.databinding.RowSearchResultBinding

class SearchViewHolder(_binding: ViewBinding) : BaseViewHolder<AbstractSearchVisitable>(_binding) {
	override fun bind(visitable: AbstractSearchVisitable) {
		val binding = _binding as RowSearchResultBinding
		val searchItem = (visitable as SearchVisitable).searchItem
		binding.lblSearchResultsCompanyName.text = searchItem.title
		binding.lblSearchResultsAddress.text = searchItem.addressSnippet
		binding.lblSearchResultsActiveStatus.text = searchItem.companyStatus
		binding.lblSearchResultsIncorporated.text = searchItem.description
	}
}
