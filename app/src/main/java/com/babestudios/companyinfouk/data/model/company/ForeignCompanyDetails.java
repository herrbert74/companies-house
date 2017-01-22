package com.babestudios.companyinfouk.data.model.company;

import com.google.gson.annotations.SerializedName;

public class ForeignCompanyDetails {

	@SerializedName("accounting_requirement")
	public AccountingRequirement accountingRequirement;

	@SerializedName("accounts")
	public ForeignCompanyAccounts accounts;

	@SerializedName("business_activity")
	public String businessActivity;

	@SerializedName("company_type")
	public String companyType;

	@SerializedName("governed_by")
	public String governedBy;

	@SerializedName("is_a_credit_finance_institution")
	public String isACreditFinanceInstitution;

	@SerializedName("originating_registry")
	public OriginatingRegistry originatingRegistry;

	@SerializedName("registration_number")
	public String registrationNumber;

}