package com.babestudios.companyinfouk.data.model.officers;


import com.google.gson.annotations.SerializedName;

public class Address {
	@SerializedName("locality")
	public String locality;
	@SerializedName("premises")
	public String premises;
	@SerializedName("postal_code")
	public String postalCode;
	@SerializedName("country")
	public String country;
	@SerializedName("address_line_1")
	public String addressLine1;
	@SerializedName("region")
	public String region;
}
