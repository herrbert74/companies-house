package com.babestudios.companieshouse.data.model.search;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CompanySearchResultItem {

	@SerializedName("companyLinks")
	public Links links;

	@SerializedName("description_identifier")
	public List<String> descriptionIdentifier = new ArrayList<String>();

	@SerializedName("date_of_creation")
	public String dateOfCreation;

	@SerializedName("snippet")
	public String snippet;

	@SerializedName("company_number")
	public String companyNumber;

	@SerializedName("title")
	public String title;

	@SerializedName("company_status")
	public String companyStatus;

	@SerializedName("matches")
	public Matches matches;

	@SerializedName("address")
	public Address address;

	@SerializedName("description")
	public String description;

	@SerializedName("kind")
	public String kind;

	@SerializedName("company_type")
	public String companyType;

	@SerializedName("address_snippet")
	public String addressSnippet;

}
