package com.babestudios.companyinfouk.data.model.company

import com.google.gson.annotations.SerializedName

import javax.annotation.Generated

class RegisteredOfficeAddress {

	@SerializedName("address_line_1")
	var addressLine1: String? = null

	@SerializedName("address_line_2")
	var addressLine2: String? = null

	@SerializedName("care_of")
	var careOf: String? = null

	@SerializedName("country")
	var country: String? = null

	@SerializedName("locality")
	var locality: String? = null

	@SerializedName("po_box")
	var poBox: String? = null

	@SerializedName("postal_code")
	var postalCode: String? = null

	@SerializedName("premises")
	var premises: String? = null

	@SerializedName("region")
	var region: String? = null

}
