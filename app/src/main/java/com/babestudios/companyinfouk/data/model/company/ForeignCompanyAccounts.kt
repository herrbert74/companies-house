package com.babestudios.companyinfouk.data.model.company


import com.google.gson.annotations.SerializedName

class ForeignCompanyAccounts {
	@SerializedName("account_period_from")
	var accountPeriodFrom: AccountPeriodFrom? = null

	@SerializedName("account_period_to")
	var accountPeriodTo: AccountPeriodTo? = null

	@SerializedName("must_file_within")
	var mustFileWithin: MustFileWithin? = null
}
