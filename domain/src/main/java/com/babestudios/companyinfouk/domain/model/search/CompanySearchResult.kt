package com.babestudios.companyinfouk.domain.model.search

import com.google.gson.annotations.SerializedName

class CompanySearchResult {

	@SerializedName("total_results")
	var totalResults: Int? = null

	@SerializedName("items")
	var items: MutableList<CompanySearchResultItem> = ArrayList()

}
