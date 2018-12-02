package com.babestudios.companyinfouk.data.model.company

import com.google.gson.annotations.SerializedName

class PreviousCompanyName {

	@SerializedName("ceased_on")
	var ceasedOn: String? = null

	@SerializedName("effective_from")
	var effectiveFrom: String? = null

	@SerializedName("name")
	var name: String? = null

}