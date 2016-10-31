
package com.babestudios.companieshouse.data.model.company;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

public class RegisteredOfficeAddress {

	@SerializedName("address_line_1")
	public String addressLine1;

	@SerializedName("address_line_2")
	public String addressLine2;

	@SerializedName("care_of")
	public String careOf;

	@SerializedName("country")
	public String country;

	@SerializedName("locality")
	public String locality;

	@SerializedName("po_box")
	public String poBox;

	@SerializedName("postal_code")
	public String postalCode;

	@SerializedName("premises")
	public String premises;

	@SerializedName("region")
	public String region;

}
