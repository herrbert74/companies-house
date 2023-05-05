package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
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
) : Parcelable
