package com.babestudios.companyinfouk.data.model.filinghistory


import com.babestudios.companyinfouk.common.model.filinghistory.*
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class FilingHistory {
	@SerializedName("start_index")
	var startIndex: Int? = null
	@SerializedName("items_per_page")
	var itemsPerPage: Int? = null
	@SerializedName("items")
	var items: List<FilingHistoryItem> = ArrayList()
	@SerializedName("total_count")
	var totalCount: Int? = null
	@SerializedName("filing_history_status")
	var filingHistoryStatus: String? = null
}

fun FilingHistory.convertToDto(): FilingHistoryDto {
	return FilingHistoryDto(
			startIndex = this.startIndex ?: 0,
			itemsPerPage = this.itemsPerPage ?: 0,
			items = this.items.map { it.convertToDto() },
			totalCount = this.totalCount ?: 0,
			filingHistoryStatus = this.filingHistoryStatus ?: ""
	)
}

fun FilingHistoryItem.convertToDto(): FilingHistoryItemDto {
	return FilingHistoryItemDto(
			date = this.date ?: "",
			type = this.type ?: "",
			links = this.links?.convertToDto() ?: FilingHistoryLinksDto(),
			category = this.category?.convertToDto() ?: CategoryDto.CATEGORY_SHOW_ALL,
			subcategory = this.subcategory ?: "",
			description = this.description ?: "",
			descriptionValues = this.descriptionValues?.convertToDto() ?: DescriptionValuesDto(),
			pages = this.pages ?: 0
	)
}

fun FilingHistoryLinks.convertToDto(): FilingHistoryLinksDto {
	return FilingHistoryLinksDto(
			documentMetadata = this.documentMetadata ?: "",
			self = this.self ?: ""
	)
}

fun Category.convertToDto(): CategoryDto {
	return when (this) {
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

fun CategoryDto.convertToDomainModel(): Category {
	return when (this) {
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
	}
}

fun DescriptionValues.convertToDto(): DescriptionValuesDto {
	return DescriptionValuesDto(
			madeUpDate = this.madeUpDate ?: "",
			officerName = this.officerName ?: "",
			appointmentDate = this.appointmentDate ?: "",
			terminationDate = this.terminationDate ?: "",
			newDate = this.newDate ?: "",
			changeDate = this.changeDate ?: "",
			oldAddress = this.oldAddress ?: "",
			newAddress = this.newAddress ?: "",
			formAttached = this.formAttached ?: "",
			chargeNumber = this.chargeNumber ?: "",
			chargeCreationDate = this.chargeCreationDate ?: "",
			date = this.date ?: "",
			capital = this.capital.map { it.convertToDto() },
			description = this.description ?: ""
	)
}

fun Capital.convertToDto(): CapitalDto {
	return CapitalDto(
			figure = this.figure ?: "",
			currency = this.currency ?: ""
	)
}
