package com.babestudios.companyinfouk.data.model.persons


import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Person {
	@SerializedName("notified_on")
	var notifiedOn: String? = null
	@SerializedName("kind")
	var kind: String? = null
	@SerializedName("country_of_residence")
	var countryOfResidence: String? = null
	@SerializedName("etag")
	var etag: String? = null
	@SerializedName("date_of_birth")
	var dateOfBirth: DateOfBirth? = null
	@SerializedName("address")
	var address: Address? = null
	@SerializedName("links")
	var links: Links_? = null
	@SerializedName("name_elements")
	var nameElements: NameElements? = null
	@SerializedName("natures_of_control")
	var naturesOfControl: List<String> = ArrayList()
	var nationality: String? = null
	var name: String? = null
	var identification: Identification? = null
}
