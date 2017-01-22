package com.babestudios.companyinfouk.data.model.company;


import com.google.gson.annotations.SerializedName;

public class ForeignCompanyAccounts {
	@SerializedName("account_period_from")
	public AccountPeriodFrom accountPeriodFrom;

	@SerializedName("account_period_to")
	public AccountPeriodTo accountPeriodTo;

	@SerializedName("must_file_within")
	public MustFileWithin mustFileWithin;
}
