package com.babestudios.companyinfouk.data.model.insolvency


import com.google.gson.annotations.SerializedName

class Practitioner {
	@SerializedName("address")
	var address: Address? = null
	@SerializedName("appointed_on")
	var appointedOn: String? = null
	@SerializedName("ceased_to_act_on")
	var ceasedToActOn: String? = null
	@SerializedName("name")
	var name: String? = null
	@SerializedName("role")
	var role: String? = null
}
