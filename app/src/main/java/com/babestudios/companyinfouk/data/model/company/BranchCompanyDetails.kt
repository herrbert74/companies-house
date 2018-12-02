package com.babestudios.companyinfouk.data.model.company

import com.google.gson.annotations.SerializedName

class BranchCompanyDetails {

	@SerializedName("business_activity")
	var businessActivity: String? = null

	@SerializedName("parent_company_name")
	var parentCompanyName: String? = null

	@SerializedName("parent_company_number")
	var parentCompanyNumber: String? = null
}

