package com.babestudios.companyinfouk.shared.data.mapper

import com.babestudios.base.data.mapNullInputList
import com.babestudios.companyinfouk.shared.data.local.apilookup.FilingHistoryDescriptionsHelper
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

@Suppress("CyclomaticComplexMethod")
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
		CategoryDto.CATEGORY_NAME -> Category.CATEGORY_NAME
		CategoryDto.CATEGORY_AUDITORS -> Category.CATEGORY_AUDITORS
		CategoryDto.CATEGORY_RESOLUTION -> Category.CATEGORY_RESOLUTION
		CategoryDto.CATEGORY_MORTGAGE -> Category.CATEGORY_MORTGAGE
		CategoryDto.CATEGORY_PERSONS -> Category.CATEGORY_PERSONS
		CategoryDto.CATEGORY_MISCELLANEOUS -> Category.CATEGORY_MISCELLANEOUS //Winnow Solutions
		CategoryDto.CATEGORY_DOCUMENT_REPLACEMENT -> Category.CATEGORY_DOCUMENT_REPLACEMENT //Winnow Solutions
		CategoryDto.CATEGORY_RETURN -> Category.CATEGORY_RETURN //Legacy
		null -> Category.CATEGORY_SHOW_ALL
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
