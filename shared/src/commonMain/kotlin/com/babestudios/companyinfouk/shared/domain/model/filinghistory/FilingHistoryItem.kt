package com.babestudios.companyinfouk.shared.domain.model.filinghistory

import kotlinx.serialization.Serializable

@Serializable
data class FilingHistoryItem(
	val date: String = "",
	val type: String = "",
	val links: FilingHistoryLinks = FilingHistoryLinks(),
	val category: Category = Category.CATEGORY_SHOW_ALL,
	val subcategory: String? = null,
	val description: String = "",
	val pages: Int = 0,
)
