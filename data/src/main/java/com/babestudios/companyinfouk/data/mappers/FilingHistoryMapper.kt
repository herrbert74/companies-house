package com.babestudios.companyinfouk.data.mappers

import com.babestudios.base.data.mapNullInputList
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.shared.data.model.filinghistory.CategoryDto
import com.babestudios.companyinfouk.shared.data.model.filinghistory.DescriptionValuesDto
import com.babestudios.companyinfouk.shared.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.shared.data.model.filinghistory.FilingHistoryItemDto
import com.babestudios.companyinfouk.shared.data.model.filinghistory.FilingHistoryLinksDto
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryLinks

fun FilingHistoryDto.toFilingHistory() = FilingHistory(
	startIndex ?: 0,
	itemsPerPage ?: 0,
	mapFilingHistoryItemList(items),
	totalCount ?: 0,
	filingHistoryStatus.orEmpty()
)

private fun mapFilingHistoryItemList(
	items: List<FilingHistoryItemDto>?,
) = mapNullInputList(items) { itemDto ->
	itemDto.mapFilingHistoryItemDto(
		{ linksDto ->
			mapFilingHistoryLinks(linksDto)
		}
	) { categoryDto -> mapFilingHistoryCategoryDto(categoryDto) }
}

private fun FilingHistoryItemDto.mapFilingHistoryItemDto(
	mapFilingHistoryLinks: (FilingHistoryLinksDto?) -> FilingHistoryLinks,
	mapCategoryDto: (CategoryDto?) -> Category,
): FilingHistoryItem = FilingHistoryItem(
	date.orEmpty(),
	type.orEmpty(),
	mapFilingHistoryLinks(links),
	mapCategoryDto(category),
	subcategory,
	formatFilingHistoryDescriptionDto(
		FilingHistoryDescriptionsHelper.filingHistoryLookUp(description.orEmpty()),
		descriptionValues
	),
	pages ?: 0
)

private fun mapFilingHistoryLinks(input: FilingHistoryLinksDto?): FilingHistoryLinks {
	return FilingHistoryLinks(input?.documentMetadata.orEmpty(), input?.self.orEmpty())
}

private fun mapFilingHistoryCategoryDto(input: CategoryDto?): Category {
	return when (input) {
		CategoryDto.CATEGORY_SHOW_ALL -> Category.CATEGORY_SHOW_ALL
		CategoryDto.CATEGORY_GAZETTE -> Category.CATEGORY_GAZETTE
		CategoryDto.CATEGORY_CONFIRMATION_STATEMENT -> Category.CATEGORY_CONFIRMATION_STATEMENT
		CategoryDto.CATEGORY_ACCOUNTS -> Category.CATEGORY_ACCOUNTS
		CategoryDto.CATEGORY_ANNUAL_RETURN -> Category.CATEGORY_ANNUAL_RETURN
		CategoryDto.CATEGORY_OFFICERS -> Category.CATEGORY_OFFICERS
		CategoryDto.CATEGORY_ADDRESS -> Category.CATEGORY_ADDRESS
		CategoryDto.CATEGORY_CAPITAL -> Category.CATEGORY_CAPITAL
		CategoryDto.CATEGORY_INSOLVENCY -> Category.CATEGORY_INSOLVENCY
		CategoryDto.CATEGORY_OTHER -> Category.CATEGORY_OTHER
		CategoryDto.CATEGORY_INCORPORATION -> Category.CATEGORY_INCORPORATION
		CategoryDto.CATEGORY_CONSTITUTION -> Category.CATEGORY_CONSTITUTION
		CategoryDto.CATEGORY_AUDITORS -> Category.CATEGORY_AUDITORS
		CategoryDto.CATEGORY_RESOLUTION -> Category.CATEGORY_RESOLUTION
		CategoryDto.CATEGORY_MORTGAGE -> Category.CATEGORY_MORTGAGE
		CategoryDto.CATEGORY_PERSONS -> Category.CATEGORY_PERSONS
		null -> Category.CATEGORY_SHOW_ALL
	}
}

fun mapFilingHistoryCategory(input: Category): CategoryDto {
	return when (input) {
		Category.CATEGORY_SHOW_ALL -> CategoryDto.CATEGORY_SHOW_ALL
		Category.CATEGORY_GAZETTE -> CategoryDto.CATEGORY_GAZETTE
		Category.CATEGORY_CONFIRMATION_STATEMENT -> CategoryDto.CATEGORY_CONFIRMATION_STATEMENT
		Category.CATEGORY_ACCOUNTS -> CategoryDto.CATEGORY_ACCOUNTS
		Category.CATEGORY_ANNUAL_RETURN -> CategoryDto.CATEGORY_ANNUAL_RETURN
		Category.CATEGORY_OFFICERS -> CategoryDto.CATEGORY_OFFICERS
		Category.CATEGORY_ADDRESS -> CategoryDto.CATEGORY_ADDRESS
		Category.CATEGORY_CAPITAL -> CategoryDto.CATEGORY_CAPITAL
		Category.CATEGORY_INSOLVENCY -> CategoryDto.CATEGORY_INSOLVENCY
		Category.CATEGORY_OTHER -> CategoryDto.CATEGORY_OTHER
		Category.CATEGORY_INCORPORATION -> CategoryDto.CATEGORY_INCORPORATION
		Category.CATEGORY_CONSTITUTION -> CategoryDto.CATEGORY_CONSTITUTION
		Category.CATEGORY_AUDITORS -> CategoryDto.CATEGORY_AUDITORS
		Category.CATEGORY_RESOLUTION -> CategoryDto.CATEGORY_RESOLUTION
		Category.CATEGORY_MORTGAGE -> CategoryDto.CATEGORY_MORTGAGE
		Category.CATEGORY_PERSONS -> CategoryDto.CATEGORY_PERSONS
	}
}

fun formatFilingHistoryDescriptionDto(description: String, descriptionValues: DescriptionValuesDto?): String {
	val matchResult = "\\{.*?\\}".toRegex().findAll(description) //Search anything within braces
	var result = description
	matchResult.iterator().forEach {
		val key = it.groupValues[0].replace("""[{}]""".toRegex(), "") //Remove braces from key
		//Replace from mapping, throw away objects like Capital, which is not used
		result = result.replace(it.groupValues[0], descriptionValues?.pairs?.get(key) ?: "")
	}
	//Simply replace "legacy" and "miscellaneous" descriptions with the descriptionValue.description
	@Suppress("SwallowedException")
	result = try {
		result.replace(
			"(legacy|miscellaneous)".toRegex(RegexOption.IGNORE_CASE),
			descriptionValues?.pairs?.get("description") ?: ""
		)
	} catch (e: IllegalArgumentException) {
		//Above replace calls through to replaceAll, which uses regexes, but some legacy strings are invalid
		descriptionValues?.pairs?.get("description") ?: ""
	}
	return result
}
