package com.babestudios.companyinfouk.charges.ui.details.list

import com.babestudios.companyinfouk.data.model.charges.Transaction

@Suppress("UnnecessaryAbstractClass")
abstract class AbstractChargeDetailsVisitable {
	abstract fun type(chargeDetailsTypeFactory: ChargeDetailsAdapter.ChargeDetailsTypeFactory): Int
}

class ChargeDetailsHeaderVisitable(val chargeDetailsHeaderItem: ChargeDetailsHeaderItem)
	: AbstractChargeDetailsVisitable() {
	override fun type(chargeDetailsTypeFactory: ChargeDetailsAdapter.ChargeDetailsTypeFactory): Int {
		return chargeDetailsTypeFactory.type(chargeDetailsHeaderItem)
	}
}

class ChargeDetailsVisitable(val transaction: Transaction) : AbstractChargeDetailsVisitable() {
	override fun type(chargeDetailsTypeFactory: ChargeDetailsAdapter.ChargeDetailsTypeFactory): Int {
		return chargeDetailsTypeFactory.type(transaction)
	}
}
