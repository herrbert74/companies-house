package com.babestudios.companieshouse.data.model.officers;


import com.google.gson.annotations.SerializedName;

public class OfficerItem {
	@SerializedName("address")
	public Address address;
	@SerializedName("identification")
	public Identification identification;
	@SerializedName("appointed_on")
	public String appointedOn;
	@SerializedName("links")
	public OfficerLinks links;
	@SerializedName("name")
	public String name;
	@SerializedName("officer_role")
	public String officerRole;
	@SerializedName("date_of_birth")
	public DateOfBirth dateOfBirth;
	@SerializedName("occupation")
	public String occupation;
	@SerializedName("country_of_residence")
	public String countryOfResidence;
	@SerializedName("nationality")
	public String nationality;
	@SerializedName("resigned_on")
	public String resignedOn;
}
