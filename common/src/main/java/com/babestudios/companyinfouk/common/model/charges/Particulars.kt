package com.babestudios.companyinfouk.common.model.charges


class Particulars(
		var containsFixedCharge: Boolean? = false,
		var containsFloatingCharge: Boolean? = false,
		var containsNegativePledge: Boolean? = false,
		var description: String = "",
		var floatingChargeCoversAll: Boolean? = false,
		var type: String = ""
)
