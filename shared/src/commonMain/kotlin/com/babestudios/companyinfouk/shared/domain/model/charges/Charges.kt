package com.babestudios.companyinfouk.shared.domain.model.charges

data class Charges(
	var items: List<ChargesItem> = emptyList(),
	var totalCount: Int = 0,
)
