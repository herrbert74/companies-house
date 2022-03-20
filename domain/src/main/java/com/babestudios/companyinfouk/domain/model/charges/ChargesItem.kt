package com.babestudios.companyinfouk.domain.model.charges


data class ChargesItem(
	val chargeCode: String = "",
	var createdOn: String = "",
	var deliveredOn: String = "",
	var personsEntitled: String = "",
	var resolvedOn: String = "",
	var satisfiedOn: String = "",
	var status: String = "",
	var transactions: List<Transaction> = emptyList(),
	val particulars: Particulars = Particulars(),
)
