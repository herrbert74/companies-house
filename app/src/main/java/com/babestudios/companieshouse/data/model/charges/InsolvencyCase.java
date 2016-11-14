package com.babestudios.companieshouse.data.model.charges;


import com.google.gson.annotations.SerializedName;

public class InsolvencyCase {
	@SerializedName("case_number")
	public String caseNumber;
	@SerializedName("links")
	public InsolvencyLinks links;
	@SerializedName("transaction_id")
	public String transactionId;
}
