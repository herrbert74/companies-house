package com.babestudios.companyinfouk.ui.charges.list

import com.babestudios.companyinfouk.data.model.charges.ChargesItem

abstract class AbstractChargesVisitable {
	abstract fun type(chargesTypeFactory: ChargesAdapter.ChargesTypeFactory): Int
}

class ChargesVisitable(val chargesItem: ChargesItem) : AbstractChargesVisitable() {
	override fun type(chargesTypeFactory: ChargesAdapter.ChargesTypeFactory): Int {
		return chargesTypeFactory.type(chargesItem)
	}
}