package com.babestudios.companyinfouk.shared.data.model.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AccountingRequirementDto(

	@SerialName("foreign_account_type")
	var foreignAccountType: String? = null,

	@SerialName("terms_of_account_publication")
	var termsOfAccountPublication: String? = null,
)
