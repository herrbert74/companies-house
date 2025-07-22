package com.babestudios.companyinfouk.shared.domain.model.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanySearchResult(

	@SerialName("total_results")
	var totalResults: Int = 0,

	@SerialName("items")
	var items: List<CompanySearchResultItem> = emptyList(),

	)
