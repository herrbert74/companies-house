package com.babestudios.companyinfouk.data.mappers

import com.babestudios.base.data.mapNullInputList
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelperContract
import com.babestudios.companyinfouk.shared.data.model.filinghistory.CategoryDto
import com.babestudios.companyinfouk.shared.data.model.filinghistory.DescriptionValuesDto
import com.babestudios.companyinfouk.shared.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.shared.data.model.filinghistory.FilingHistoryItemDto
import com.babestudios.companyinfouk.shared.data.model.filinghistory.FilingHistoryLinksDto
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryLinks

fun mapFilingHistoryDto(
	input: FilingHistoryDto,
	filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelperContract,
): FilingHistory {
	return FilingHistory(
		input.startIndex ?: 0,
		input.itemsPerPage ?: 0,
		mapFilingHistoryItemList(input.items, filingHistoryDescriptionsHelper),
		input.totalCount ?: 0,
		input.filingHistoryStatus.orEmpty()
	)
}

private fun mapFilingHistoryItemList(
	items: List<FilingHistoryItemDto>?,
	filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelperContract,
) = mapNullInputList(items) { itemDto ->
	mapFilingHistoryItemDto(
		itemDto,
		filingHistoryDescriptionsHelper,
		{ linksDto ->
			mapFilingHistoryLinks(linksDto)
		},
		{ categoryDto ->
			mapFilingHistoryCategoryDto(categoryDto)
		}
	)
}

private fun mapFilingHistoryItemDto(
	input: FilingHistoryItemDto,
	filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelperContract,
	mapFilingHistoryLinks: (FilingHistoryLinksDto?) -> FilingHistoryLinks,
	mapCategoryDto: (CategoryDto?) -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category,
): com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem {
	return com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem(
		input.date.orEmpty(),
		input.type.orEmpty(),
		mapFilingHistoryLinks(input.links),
		mapCategoryDto(input.category),
		input.subcategory,
		formatFilingHistoryDescriptionDto(
			filingHistoryDescriptionsHelper.filingHistoryLookUp(input.description.orEmpty()),
			input.descriptionValues
		),
		input.pages ?: 0
	)
}

private fun mapFilingHistoryLinks(input: FilingHistoryLinksDto?): FilingHistoryLinks {
	return FilingHistoryLinks(input?.documentMetadata.orEmpty(), input?.self.orEmpty())
}

private fun mapFilingHistoryCategoryDto(input: CategoryDto?): com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category {
	return when (input) {
		CategoryDto.CATEGORY_SHOW_ALL -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_SHOW_ALL
		CategoryDto.CATEGORY_GAZETTE -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_GAZETTE
		CategoryDto.CATEGORY_CONFIRMATION_STATEMENT -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_CONFIRMATION_STATEMENT
		CategoryDto.CATEGORY_ACCOUNTS -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_ACCOUNTS
		CategoryDto.CATEGORY_ANNUAL_RETURN -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_ANNUAL_RETURN
		CategoryDto.CATEGORY_OFFICERS -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_OFFICERS
		CategoryDto.CATEGORY_ADDRESS -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_ADDRESS
		CategoryDto.CATEGORY_CAPITAL -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_CAPITAL
		CategoryDto.CATEGORY_INSOLVENCY -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_INSOLVENCY
		CategoryDto.CATEGORY_OTHER -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_OTHER
		CategoryDto.CATEGORY_INCORPORATION -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_INCORPORATION
		CategoryDto.CATEGORY_CONSTITUTION -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_CONSTITUTION
		CategoryDto.CATEGORY_AUDITORS -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_AUDITORS
		CategoryDto.CATEGORY_RESOLUTION -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_RESOLUTION
		CategoryDto.CATEGORY_MORTGAGE -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_MORTGAGE
		CategoryDto.CATEGORY_PERSONS -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_PERSONS
		null -> com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_SHOW_ALL
	}
}

fun mapFilingHistoryCategory(input: com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category): CategoryDto {
	return when (input) {
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_SHOW_ALL -> CategoryDto.CATEGORY_SHOW_ALL
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_GAZETTE -> CategoryDto.CATEGORY_GAZETTE
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_CONFIRMATION_STATEMENT -> CategoryDto.CATEGORY_CONFIRMATION_STATEMENT
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_ACCOUNTS -> CategoryDto.CATEGORY_ACCOUNTS
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_ANNUAL_RETURN -> CategoryDto.CATEGORY_ANNUAL_RETURN
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_OFFICERS -> CategoryDto.CATEGORY_OFFICERS
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_ADDRESS -> CategoryDto.CATEGORY_ADDRESS
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_CAPITAL -> CategoryDto.CATEGORY_CAPITAL
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_INSOLVENCY -> CategoryDto.CATEGORY_INSOLVENCY
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_OTHER -> CategoryDto.CATEGORY_OTHER
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_INCORPORATION -> CategoryDto.CATEGORY_INCORPORATION
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_CONSTITUTION -> CategoryDto.CATEGORY_CONSTITUTION
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_AUDITORS -> CategoryDto.CATEGORY_AUDITORS
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_RESOLUTION -> CategoryDto.CATEGORY_RESOLUTION
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_MORTGAGE -> CategoryDto.CATEGORY_MORTGAGE
		com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category.CATEGORY_PERSONS -> CategoryDto.CATEGORY_PERSONS
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
