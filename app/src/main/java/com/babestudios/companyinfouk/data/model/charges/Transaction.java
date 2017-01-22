package com.babestudios.companyinfouk.data.model.charges;


import com.google.gson.annotations.SerializedName;

public class Transaction {
	@SerializedName("delivered_on")
	public String deliveredOn;
	@SerializedName("filing_type")
	public String filingType;
	@SerializedName("insolvency_case_number")
	public String insolvencyCaseNumber;
	@SerializedName("links")
	public TransactionLinks links;
	@SerializedName("transaction_id")
	public String transactionId;
}
