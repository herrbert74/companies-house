package com.babestudios.companyinfouk.common.model.filinghistory

data class FilingHistoryItemDto(
		val date: String = "",
		val type: String = "",
		val links: FilingHistoryLinksDto = FilingHistoryLinksDto(),
		val category: CategoryDto = CategoryDto.CATEGORY_SHOW_ALL,
		val subcategory: String = "",
		val description: String = "",
		val descriptionValues: DescriptionValuesDto = DescriptionValuesDto(),
		val pages: Int = 0)
