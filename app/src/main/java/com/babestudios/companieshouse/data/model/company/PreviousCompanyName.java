package com.babestudios.companieshouse.data.model.company;

import com.google.gson.annotations.SerializedName;

public class PreviousCompanyName {

	@SerializedName("ceased_on")
	public String ceasedOn;

	@SerializedName("effective_from")
	public String effectiveFrom;

	@SerializedName("name")
	public String name;

}