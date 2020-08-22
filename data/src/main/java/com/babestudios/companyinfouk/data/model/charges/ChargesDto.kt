package com.babestudios.companyinfouk.data.model.charges

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class ChargesDto {
	@SerializedName("etag")
	var etag: String? = null
	@SerializedName("items")
	var items: List<ChargesItemDto> = ArrayList()
	@SerializedName("part_satisfied_count")
	var partSatisfiedCount: Int? = 0
	@SerializedName("satisfied_count")
	var satisfiedCount: Int? = 0
	@SerializedName("total_count")
	var totalCount: Int? = 0
	@SerializedName("unfiltered_count")
	var unfilteredCount: Int? = 0
}
