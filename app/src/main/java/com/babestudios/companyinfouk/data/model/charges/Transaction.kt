package com.babestudios.companyinfouk.data.model.charges


import com.google.gson.annotations.SerializedName

class Transaction {
	@SerializedName("delivered_on")
	var deliveredOn: String? = null
	@SerializedName("filing_type")
	var filingType: String? = null
	@SerializedName("insolvency_case_number")
	var insolvencyCaseNumber: String? = null
	@SerializedName("links")
	var links: TransactionLinks? = null
	@SerializedName("transaction_id")
	var transactionId: String? = null
}
