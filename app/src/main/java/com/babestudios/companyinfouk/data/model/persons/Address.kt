package com.babestudios.companyinfouk.data.model.persons


import com.google.gson.annotations.SerializedName

class Address {
	@SerializedName("country")
	var country: String? = null
	@SerializedName("postal_code")
	var postalCode: String? = null
	@SerializedName("address_line_1")
	var addressLine1: String? = null
	@SerializedName("locality")
	var locality: String? = null
	@SerializedName("region")
	var region: String? = null
	@SerializedName("premises")
	var premises: String? = null
}
