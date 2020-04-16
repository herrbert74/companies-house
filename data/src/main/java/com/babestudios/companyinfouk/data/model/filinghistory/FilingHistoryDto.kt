package com.babestudios.companyinfouk.data.model.filinghistory


import com.babestudios.companyinfouk.common.model.filinghistory.*
import com.google.gson.annotations.SerializedName
import java.util.*

class FilingHistoryDto(
		@SerializedName("start_index")
		val startIndex: Int? = null,

		@SerializedName("items_per_page")
		val itemsPerPage: Int? = null,

		@SerializedName("items")
		val items: List<FilingHistoryItemDto> = ArrayList(),

		@SerializedName("total_count")
		val totalCount: Int? = null,

		@SerializedName("filing_history_status")
		val filingHistoryStatus: String? = null
)

fun FilingHistoryDto.convertToModel(): FilingHistory {
	return FilingHistory(
			startIndex = startIndex ?: 0,
			itemsPerPage = itemsPerPage ?: 0,
			items = items.map { it.convertToModel() },
			totalCount = totalCount ?: 0,
			filingHistoryStatus = filingHistoryStatus ?: ""
	)
}

fun FilingHistoryItemDto.convertToModel(): FilingHistoryItem {
	return FilingHistoryItem(
			date = this.date ?: "",
			type = this.type ?: "",
			links = this.links?.convertToModel() ?: FilingHistoryLinks(),
			category = this.category?.convertToModel() ?: Category.CATEGORY_SHOW_ALL,
			subcategory = this.subcategory ?: "",
			description = this.description ?: "",
			descriptionValues = this.descriptionValues?.convertToModel() ?: DescriptionValues(),
			pages = this.pages ?: 0
	)
}

fun FilingHistoryLinksDto.convertToModel(): FilingHistoryLinks {
	return FilingHistoryLinks(
			documentMetadata = this.documentMetadata ?: "",
			self = this.self ?: ""
	)
}

@Suppress("ComplexMethod")
fun CategoryDto.convertToModel(): Category {
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

@Suppress("ComplexMethod")
fun Category.convertToDomainModel(): CategoryDto {
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

fun DescriptionValuesDto.convertToModel(): DescriptionValues {
	return DescriptionValues(
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
			capital = this.capital.map { it.convertToModel() },
			description = this.description ?: ""
	)
}

fun CapitalDto.convertToModel(): Capital {
	return Capital(
			figure = this.figure ?: "",
			currency = this.currency ?: ""
	)
}
