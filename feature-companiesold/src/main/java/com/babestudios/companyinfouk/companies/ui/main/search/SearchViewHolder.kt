package com.babestudios.companyinfouk.companies.ui.main.search

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.companies.databinding.RowSearchResultBinding
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResultItem

class SearchViewHolder(val rawBinding: ViewBinding) : RecyclerView.ViewHolder(rawBinding.root) {

	fun bind(searchItem: CompanySearchResultItem) {
		val binding = rawBinding as RowSearchResultBinding
		binding.lblSearchResultsCompanyName.text = searchItem.title
		binding.lblSearchResultsAddress.text = searchItem.addressSnippet
		binding.lblSearchResultsActiveStatus.text = searchItem.companyStatus
		binding.lblSearchResultsIncorporated.text = searchItem.description
	}

}
