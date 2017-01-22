package com.babestudios.companyinfouk.data.model.insolvency;


import com.google.gson.annotations.SerializedName;

public class Practitioner {
	@SerializedName("address")
	public Address address;
	@SerializedName("appointed_on")
	public String appointedOn;
	@SerializedName("ceased_to_act_on")
	public String ceasedToActOn;
	@SerializedName("name")
	public String name;
	@SerializedName("role")
	public String role;
}
