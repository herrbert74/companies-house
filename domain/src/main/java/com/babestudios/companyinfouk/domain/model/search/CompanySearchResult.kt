package com.babestudios.companyinfouk.domain.model.search

import com.google.gson.annotations.SerializedName

data class CompanySearchResult (

	@SerializedName("total_results")
	var totalResults: Int = 0,

	@SerializedName("items")
	var items: List<CompanySearchResultItem> = emptyList()

)
