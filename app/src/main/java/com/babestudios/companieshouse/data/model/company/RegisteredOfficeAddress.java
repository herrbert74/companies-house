
package com.babestudios.companieshouse.data.model.company;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class RegisteredOfficeAddress {

	@SerializedName("postal_code")
	public String postalCode;

	@SerializedName("address_line_1")
	public String addressLine1;

	@SerializedName("address_line_2")
	public String addressLine2;

	@SerializedName("locality")
	public String locality;

}
