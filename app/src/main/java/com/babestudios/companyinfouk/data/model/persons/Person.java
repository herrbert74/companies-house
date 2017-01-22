package com.babestudios.companyinfouk.data.model.persons;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Person {
	@SerializedName("notified_on")
	public String notifiedOn;
	@SerializedName("kind")
	public String kind;
	@SerializedName("country_of_residence")
	public String countryOfResidence;
	@SerializedName("etag")
	public String etag;
	@SerializedName("date_of_birth")
	public DateOfBirth dateOfBirth;
	@SerializedName("address")
	public Address address;
	@SerializedName("links")
	public Links_ links;
	@SerializedName("name_elements")
	public NameElements nameElements;
	@SerializedName("natures_of_control")
	public List<String> naturesOfControl = new ArrayList<>();
	public String nationality;
	public String name;
	public Identification identification;
}
