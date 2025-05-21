package com.babestudios.companyinfouk.shared.data.model.filinghistory

import com.babestudios.companyinfouk.shared.data.network.DescriptionValueTransformingSerializer
import kotlinx.serialization.SerialName

import kotlinx.serialization.Serializable

@Suppress("LongParameterList")
@Serializable
class FilingHistoryItemDto(
	@SerialName("date")
	var date: String? = null,

	@SerialName("type")
	var type: String? = null,

	@SerialName("links")
	var links: FilingHistoryLinksDto? = null,

	@SerialName("category")
	var category: CategoryDto? = null,

	@SerialName("subcategory")
	var subcategory: String? = null,

	@SerialName("action_date")
	var actionDate: String? = null,

	@SerialName("description")
	var description: String? = null,

	@SerialName("description_values")
	@Serializable(with = DescriptionValueTransformingSerializer::class)
	var descriptionValues: DescriptionValuesDto? = null,

	@SerialName("pages")
	var pages: Int? = null,

	@SerialName("barcode")
	var barcode: String? = null,

	@SerialName("transaction_id")
	var transactionId: String? = null,

	@SerialName("associated_filings")
	var associatedFilings: List<AssociatedFilingDto> = ArrayList(),

	@SerialName("paper_filed")
	var paperFiled: Boolean? = null,
)
