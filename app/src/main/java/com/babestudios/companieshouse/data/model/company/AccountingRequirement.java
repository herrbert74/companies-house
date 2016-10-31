package com.babestudios.companieshouse.data.model.company;


import com.google.gson.annotations.SerializedName;

public class AccountingRequirement {

	@SerializedName("foreign_account_type")
	public String foreignAccountType;

	@SerializedName("terms_of_account_publication")
	public String termsOfAccountPublication;
}
