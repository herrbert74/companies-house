package com.babestudios.companyinfouk.data.model.charges


import com.google.gson.annotations.SerializedName

class ScottishAlterations {
	@SerializedName("has_alterations_to_order")
	var hasAlterationsToOrder: String? = null
	@SerializedName("has_alterations_to_prohibitions")
	var hasAlterationsToProhibitions: String? = null
	@SerializedName("has_restricting_provisions")
	var hasRestrictingProvisions: String? = null
}
