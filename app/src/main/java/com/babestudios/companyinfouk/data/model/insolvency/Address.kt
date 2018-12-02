package com.babestudios.companyinfouk.data.model.insolvency


import com.google.gson.annotations.SerializedName

class Address {
	@SerializedName("address_line_1")
	var addressLine1: String? = null
	@SerializedName("address_line_2")
	var addressLine2: String? = null
	@SerializedName("country")
	var country: String? = null
	@SerializedName("locality")
	var locality: String? = null
	@SerializedName("postal_code")
	var postalCode: String? = null
	@SerializedName("region")
	var region: String? = null
}
