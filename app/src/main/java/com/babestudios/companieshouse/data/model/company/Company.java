
package com.babestudios.companieshouse.data.model.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Company {

	@SerializedName("has_insolvency_history")
	public Boolean hasInsolvencyHistory;

	@SerializedName("registered_office_address")
	public RegisteredOfficeAddress registeredOfficeAddress;

	@SerializedName("date_of_creation")
	public String dateOfCreation;

	@SerializedName("sic_codes")
	public List<String> sicCodes = new ArrayList<>();

	@SerializedName("annual_return")
	public AnnualReturn annualReturn;

	@SerializedName("accounts")
	public Accounts accounts;

	@SerializedName("company_name")
	public String companyName;

	@SerializedName("last_full_members_list_date")
	public String lastFullMembersListDate;

	@SerializedName("etag")
	public String etag;

	@SerializedName("company_number")
	public String companyNumber;

	@SerializedName("company_status")
	public String companyStatus;

	@SerializedName("has_charges")
	public Boolean hasCharges;

	@SerializedName("type")
	public String type;

	@SerializedName("jurisdiction")
	public String jurisdiction;

	@SerializedName("undeliverable_registered_office_address")
	public Boolean undeliverableRegisteredOfficeAddress;

	@SerializedName("companyLinks")
	public CompanyLinks companyLinks;

	@SerializedName("date_of_cessation")
	public String dateOfCessation;

	@SerializedName("can_file")
	public Boolean canFile;

}
