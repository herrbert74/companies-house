
package com.babestudios.companyinfouk.data.model.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Company {

	@SerializedName("accounts")
	public Accounts accounts;

	@SerializedName("annual_return")
	public AnnualReturn annualReturn;

	@SerializedName("branch_company_details")
	public BranchCompanyDetails branchCompanyDetails;

	@SerializedName("can_file")
	public String canFile;

	@SerializedName("company_name")
	public String companyName;

	@SerializedName("company_number")
	public String companyNumber;

	@SerializedName("company_status")
	public String companyStatus;

	@SerializedName("company_status_detail")
	public String companyStatusDetail;

	@SerializedName("confirmation_statement")
	public ConfirmationStatement confirmationStatement;

	@SerializedName("date_of_cessation")
	public String dateOfCessation;

	@SerializedName("date_of_creation")
	public String dateOfCreation;

	@SerializedName("etag")
	public String etag;

	@SerializedName("foreign_company_details")
	public ForeignCompanyDetails foreignCompanyDetails;

	@SerializedName("has_been_liquidated")
	public String hasBeenLiquidated;

	@SerializedName("has_charges")
	public String hasCharges;

	@SerializedName("has_insolvency_history")
	public String hasInsolvencyHistory;

	@SerializedName("is_community_interest_company")
	public String isCommunityInterestCompany;

	@SerializedName("jurisdiction")
	public String jurisdiction;

	@SerializedName("last_full_members_list_date")
	public String lastFullMembersListDate;

	@SerializedName("links")
	public CompanyLinks links;

	@SerializedName("partial_data_available")
	public String partialDataAvailable;

	@SerializedName("previous_company_names")
	public List<PreviousCompanyName> previousCompanyNames = new ArrayList<PreviousCompanyName>();

	@SerializedName("registered_office_address")
	public RegisteredOfficeAddress registeredOfficeAddress;

	@SerializedName("registered_office_is_in_dispute")
	public String registeredOfficeIsInDispute;

	@SerializedName("sic_codes")
	public List<String> sicCodes = new ArrayList<String>();

	@SerializedName("type")
	public String type;

	@SerializedName("undeliverable_registered_office_address")
	public String undeliverableRegisteredOfficeAddress;

}
