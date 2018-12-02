package com.babestudios.companyinfouk.data.model.company


import com.google.gson.annotations.SerializedName

class AccountingRequirement {

	@SerializedName("foreign_account_type")
	var foreignAccountType: String? = null

	@SerializedName("terms_of_account_publication")
	var termsOfAccountPublication: String? = null
}
