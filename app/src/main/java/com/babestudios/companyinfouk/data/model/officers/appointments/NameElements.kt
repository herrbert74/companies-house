package com.babestudios.companyinfouk.data.model.officers.appointments


import com.google.gson.annotations.SerializedName

class NameElements {

	@SerializedName("forename")
	var forename: String? = null
	@SerializedName("honours")
	var honours: String? = null
	@SerializedName("other_forenames")
	var otherForenames: String? = null
	@SerializedName("surname")
	var surname: String? = null
	@SerializedName("title")
	var title: String? = null

}
