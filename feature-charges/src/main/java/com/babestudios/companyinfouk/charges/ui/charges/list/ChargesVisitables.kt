package com.babestudios.companyinfouk.charges.ui.charges.list

import com.babestudios.companyinfouk.common.model.charges.ChargesItem

sealed class ChargesVisitableBase {
	abstract fun type(chargesTypeFactory: ChargesAdapter.ChargesTypeFactory): Int
}

class ChargesVisitable(val chargesItem: ChargesItem) : ChargesVisitableBase() {
	override fun type(chargesTypeFactory: ChargesAdapter.ChargesTypeFactory): Int {
		return chargesTypeFactory.type(chargesItem)
	}
}

