package com.babestudios.companyinfouk.data.mappers

import com.babestudios.companyinfouk.common.model.filinghistory.Category
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryLinks
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelperContract
import com.babestudios.companyinfouk.data.model.filinghistory.CategoryDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItemDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryLinksDto

inline fun mapFilingHistoryDto(
		input: FilingHistoryDto,
		mapItems: (List<FilingHistoryItemDto>?) -> List<FilingHistoryItem>
): FilingHistory {
	return FilingHistory(
			input.startIndex ?: 0,
			input.itemsPerPage ?: 0,
			mapItems(input.items),
			input.totalCount ?: 0,
			input.filingHistoryStatus.orEmpty()
	)
}

fun mapFilingHistoryItemDto(
		input: FilingHistoryItemDto,
		filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelperContract,
		mapFilingHistoryLinks: (FilingHistoryLinksDto?) -> FilingHistoryLinks,
		mapCategoryDto: (CategoryDto?) -> Category
): FilingHistoryItem {
	return FilingHistoryItem(
			input.date.orEmpty(),
			input.type.orEmpty(),
			mapFilingHistoryLinks(input.links),
			mapCategoryDto(input.category),
			input.subcategory.orEmpty(),
			formatFilingHistoryDescriptionDto(
					filingHistoryDescriptionsHelper.filingHistoryLookUp(input.description.orEmpty()),
					input.descriptionValues
			),
			input.pages ?: 0
	)
}

fun mapFilingHistoryLinks(input: FilingHistoryLinksDto?): FilingHistoryLinks {
	return FilingHistoryLinks(input?.documentMetadata.orEmpty(), input?.self.orEmpty())
}

fun mapFilingHistoryCategoryDto(input: CategoryDto?): Category {
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
		else -> Category.CATEGORY_SHOW_ALL
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
	}
}

fun formatFilingHistoryDescriptionDto(description: String, descriptionValues: Map<String, Any>?): String {
	@Suppress("RegExpRedundantEscape")
	val matchResult = "\\{.*?\\}".toRegex().findAll(description) //Search anything within braces
	var result = description
	matchResult.iterator().forEach {
		val key = it.groupValues[0].replace("""[{}]""".toRegex(), "") //Remove braces from key
		//Replace from mapping, throw away objects like Capital, which is not used
		result = result.replace(it.groupValues[0], descriptionValues?.get(key) as? String ?: "")
	}
	//Simply replace "legacy" and "miscellaneous" descriptions with the descriptionValue.description
	result = try {
		result.replace(
				"(legacy|miscellaneous)".toRegex(RegexOption.IGNORE_CASE),
				descriptionValues?.get("description") as? String ?: ""
		)
	} catch (e: IllegalArgumentException) {
		//Above replace calls through to replaceAll, which uses regexes, but some legacy strings are invalid
		descriptionValues?.get("description") as? String ?: ""
	}
	return result
}
