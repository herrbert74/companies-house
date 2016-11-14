package com.babestudios.companieshouse.data.model.charges;


import com.google.gson.annotations.SerializedName;

public class Particulars {
	@SerializedName("chargor_acting_as_bare_trustee")
	public String chargorActingAsBareTrustee;
	@SerializedName("contains_fixed_charge")
	public String containsFixedCharge;
	@SerializedName("contains_floating_charge")
	public String containsFloatingCharge;
	@SerializedName("contains_negative_pledge")
	public String containsNegativePledge;
	@SerializedName("description")
	public String description;
	@SerializedName("floating_charge_covers_all")
	public String floatingChargeCoversAll;
	@SerializedName("type")
	public String type;
}
