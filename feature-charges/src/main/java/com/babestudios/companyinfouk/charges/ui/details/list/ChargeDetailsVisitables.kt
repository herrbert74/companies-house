package com.babestudios.companyinfouk.charges.ui.details.list

import com.babestudios.companyinfouk.domain.model.charges.Transaction

sealed class ChargeDetailsVisitableBase {
	abstract fun type(chargeDetailsTypeFactory: ChargeDetailsAdapter.ChargeDetailsTypeFactory): Int
}

class ChargeDetailsHeaderVisitable(val chargeDetailsHeaderItem: ChargeDetailsHeaderItem)
	: ChargeDetailsVisitableBase() {
	override fun type(chargeDetailsTypeFactory: ChargeDetailsAdapter.ChargeDetailsTypeFactory): Int {
		return chargeDetailsTypeFactory.type(chargeDetailsHeaderItem)
	}
}

class ChargeDetailsVisitable(val transaction: Transaction) : ChargeDetailsVisitableBase() {
	override fun type(chargeDetailsTypeFactory: ChargeDetailsAdapter.ChargeDetailsTypeFactory): Int {
		return chargeDetailsTypeFactory.type(transaction)
	}
}
