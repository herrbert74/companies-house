package com.babestudios.companyinfouk.data.model.persons


import com.google.gson.annotations.SerializedName

class NameElements {
	@SerializedName("surname")
	var surname: String? = null
	@SerializedName("title")
	var title: String? = null
	@SerializedName("middle_name")
	var middleName: String? = null
	@SerializedName("forename")
	var forename: String? = null
}
