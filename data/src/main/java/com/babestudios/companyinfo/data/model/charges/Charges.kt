package com.babestudios.companyinfo.data.model.charges

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Charges {
	@SerializedName("etag")
	var etag: String? = null
	@SerializedName("items")
	var items: List<ChargesItem> = ArrayList()
	@SerializedName("part_satisfied_count")
	var partSatisfiedCount: String? = null
	@SerializedName("satisfied_count")
	var satisfiedCount: String? = null
	@SerializedName("total_count")
	var totalCount: String? = null
	@SerializedName("unfiletered_count")
	var unfileteredCount: String? = null
}
