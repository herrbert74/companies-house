package com.babestudios.companyinfouk.data.model.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BranchCompanyDetailsDto(
	@SerialName("business_activity")
	var businessActivity: String? = null,

	@SerialName("parent_company_name")
	var parentCompanyName: String? = null,

	@SerialName("parent_company_number")
	var parentCompanyNumber: String? = null,
)

