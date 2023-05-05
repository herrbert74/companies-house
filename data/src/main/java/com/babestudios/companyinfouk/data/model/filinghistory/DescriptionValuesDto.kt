package com.babestudios.companyinfouk.data.model.filinghistory

import kotlinx.serialization.Serializable

/**
 * Created to serialize descriptionValues field in [FilingHistoryItemDto] with a serializer.
 */
@Serializable
data class DescriptionValuesDto(
	var pairs: Map<String, String>
)
