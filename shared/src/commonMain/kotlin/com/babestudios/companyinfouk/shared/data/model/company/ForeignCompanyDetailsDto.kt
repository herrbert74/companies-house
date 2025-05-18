package com.babestudios.companyinfouk.shared.data.model.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("LongParameterList")
@Serializable
class ForeignCompanyDetailsDto(

	@SerialName("accounting_requirement")
	var accountingRequirement: AccountingRequirementDto? = null,

	@SerialName("accounts")
	var accounts: ForeignCompanyAccountsDto? = null,

	@SerialName("business_activity")
	var businessActivity: String? = null,

	@SerialName("company_type")
	var companyType: String? = null,

	@SerialName("governed_by")
	var governedBy: String? = null,

	@SerialName("is_a_credit_finance_institution")
	var isACreditFinanceInstitution: String? = null,

	@SerialName("originating_registry")
	var originatingRegistry: OriginatingRegistryDto? = null,

	@SerialName("registration_number")
	var registrationNumber: String? = null,
)
