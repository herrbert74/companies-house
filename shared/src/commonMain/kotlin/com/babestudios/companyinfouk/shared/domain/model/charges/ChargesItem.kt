package com.babestudios.companyinfouk.shared.domain.model.charges

import kotlinx.serialization.Serializable

@Serializable
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
