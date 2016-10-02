package com.babestudios.companieshouse.data.model.search;

import com.google.gson.annotations.SerializedName;

public class Address {

	@SerializedName("region")
	public String region;

	@SerializedName("postal_code")
	public String postalCode;

	@SerializedName("address_line_1")
	public String addressLine1;

	@SerializedName("locality")
	public String locality;

	@SerializedName("premises")
	public String premises;

}