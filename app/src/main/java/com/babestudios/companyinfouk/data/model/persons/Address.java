package com.babestudios.companyinfouk.data.model.persons;


import com.google.gson.annotations.SerializedName;

public class Address {
	@SerializedName("country")
	public String country;
	@SerializedName("postal_code")
	public String postalCode;
	@SerializedName("address_line_1")
	public String addressLine1;
	@SerializedName("locality")
	public String locality;
	@SerializedName("region")
	public String region;
	@SerializedName("premises")
	public String premises;
}
