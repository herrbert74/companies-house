package com.babestudios.companyinfouk.data.model.charges


import com.google.gson.annotations.SerializedName

class InsolvencyCase {
	@SerializedName("case_number")
	var caseNumber: String? = null
	@SerializedName("links")
	var links: InsolvencyLinks? = null
	@SerializedName("transaction_id")
	var transactionId: String? = null
}
