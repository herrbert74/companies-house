package com.babestudios.companyinfouk.data.model.charges;


import com.google.gson.annotations.SerializedName;

public class ScottishAlterations {
	@SerializedName("has_alterations_to_order")
	public String hasAlterationsToOrder;
	@SerializedName("has_alterations_to_prohibitions")
	public String hasAlterationsToProhibitions;
	@SerializedName("has_restricting_provisions")
	public String hasRestrictingProvisions;
}
