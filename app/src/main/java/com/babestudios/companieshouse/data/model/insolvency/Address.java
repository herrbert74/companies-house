package com.babestudios.companieshouse.data.model.insolvency;


import com.google.gson.annotations.SerializedName;

public class Address {
	@SerializedName("address_line_1")
	public String addressLine1;
	@SerializedName("address_line_2")
	public String addressLine2;
	@SerializedName("country")
	public String country;
	@SerializedName("locality")
	public String locality;
	@SerializedName("postal_code")
	public String postalCode;
	@SerializedName("region")
	public String region;
}
