package com.babestudios.companyinfouk.data.model.charges


import com.google.gson.annotations.SerializedName

class SecuredDetails {
	@SerializedName("description")
	var description: String? = null
	@SerializedName("type")
	var type: String? = null
}
