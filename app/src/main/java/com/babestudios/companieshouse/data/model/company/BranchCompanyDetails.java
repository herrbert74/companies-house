package com.babestudios.companieshouse.data.model.company;

import com.google.gson.annotations.SerializedName;

public class BranchCompanyDetails {

	@SerializedName("business_activity")
	public String businessActivity;

	@SerializedName("parent_company_name")
	public String parentCompanyName;

	@SerializedName("parent_company_number")
	public String parentCompanyNumber;
}

