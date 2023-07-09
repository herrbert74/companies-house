package com.babestudios.companyinfouk.shared.data.model.charges

import kotlinx.serialization.SerialName

import kotlinx.serialization.Serializable

@Serializable
class ChargesDto {
	@SerialName("etag")
	var etag: String? = null
	@SerialName("items")
	var items: List<ChargesItemDto> = ArrayList()
	@SerialName("part_satisfied_count")
	var partSatisfiedCount: Int? = 0
	@SerialName("satisfied_count")
	var satisfiedCount: Int? = 0
	@SerialName("total_count")
	var totalCount: Int? = 0
	@SerialName("unfiltered_count")
	var unfilteredCount: Int? = 0
}
