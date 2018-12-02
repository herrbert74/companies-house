package com.babestudios.companyinfouk.data.model.charges


import com.google.gson.annotations.SerializedName

class TransactionLinks {
	@SerializedName("filing")
	var filing: String? = null
	@SerializedName("insolvency_case")
	var insolvencyCase: String? = null

}
