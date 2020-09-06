package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ForeignCompanyDetailsDto (

		@SerializedName("accounting_requirement")
	var accountingRequirement: AccountingRequirementDto? = null,

		@SerializedName("accounts")
	var accounts: ForeignCompanyAccountsDto? = null,

		@SerializedName("business_activity")
	var businessActivity: String? = null,

		@SerializedName("company_type")
	var companyType: String? = null,

		@SerializedName("governed_by")
	var governedBy: String? = null,

		@SerializedName("is_a_credit_finance_institution")
	var isACreditFinanceInstitution: String? = null,

		@SerializedName("originating_registry")
	var originatingRegistry: OriginatingRegistryDto? = null,

		@SerializedName("registration_number")
	var registrationNumber: String? = null
) : Parcelable
