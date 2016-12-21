package com.babestudios.companieshouse.data.model.charges;


import com.google.gson.annotations.SerializedName;

public class Particulars {
	@SerializedName("chargor_acting_as_bare_trustee")
	public String chargorActingAsBareTrustee;
	@SerializedName("contains_fixed_charge")
	public boolean containsFixedCharge;
	@SerializedName("contains_floating_charge")
	public boolean containsFloatingCharge;
	@SerializedName("contains_negative_pledge")
	public boolean containsNegativePledge;
	@SerializedName("description")
	public String description;
	@SerializedName("floating_charge_covers_all")
	public boolean floatingChargeCoversAll;
	@SerializedName("type")
	public String type;
}
