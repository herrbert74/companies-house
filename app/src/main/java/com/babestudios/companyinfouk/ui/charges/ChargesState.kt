package com.babestudios.companyinfouk.ui.charges

import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.ui.charges.list.AbstractChargesVisitable

enum class ContentChange {
	NONE,
	CHARGES_RECEIVED
}

data class ChargesState(
		var chargeItems: List<AbstractChargesVisitable> = ArrayList()
) : BaseState() {
	var totalCount: Int? = null
	var parameter: String? = ""
	var contentChange: ContentChange = ContentChange.NONE
}