package com.babestudios.companyinfouk.data.model.search

import com.google.gson.annotations.SerializedName

class Address {

	@SerializedName("region")
	var region: String? = null

	@SerializedName("postal_code")
	var postalCode: String? = null

	@SerializedName("address_line_1")
	var addressLine1: String? = null

	@SerializedName("locality")
	var locality: String? = null

	@SerializedName("premises")
	var premises: String? = null

}