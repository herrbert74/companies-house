package com.babestudios.companyinfouk.data.model.officers;


import android.support.annotation.NonNull;

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
	@NonNull
	public String occupation = "Unknown";
	@SerializedName("country_of_residence")
	public String countryOfResidence = "Unknown";
	@SerializedName("nationality")
	public String nationality = "Unknown";
	@SerializedName("resigned_on")
	public String resignedOn;
}
