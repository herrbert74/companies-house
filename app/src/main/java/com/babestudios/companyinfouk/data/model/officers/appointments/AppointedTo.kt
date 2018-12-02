package com.babestudios.companyinfouk.data.model.officers.appointments


import com.google.gson.annotations.SerializedName

class AppointedTo {

	@SerializedName("company_name")
	var companyName: String? = null
	@SerializedName("company_number")
	var companyNumber: String? = null
	@SerializedName("company_status")
	var companyStatus: String? = null

}