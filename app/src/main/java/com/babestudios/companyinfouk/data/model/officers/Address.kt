package com.babestudios.companyinfouk.data.model.officers


import com.google.gson.annotations.SerializedName

class Address {
	@SerializedName("locality")
	var locality: String? = null
	@SerializedName("premises")
	var premises: String? = null
	@SerializedName("postal_code")
	var postalCode: String? = null
	@SerializedName("country")
	var country: String? = null
	@SerializedName("address_line_1")
	var addressLine1: String? = null
	@SerializedName("region")
	var region: String? = null
}
