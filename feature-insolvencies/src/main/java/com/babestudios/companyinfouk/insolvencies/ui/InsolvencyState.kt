package com.babestudios.companyinfouk.insolvencies.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.charges.ui.charges.list.AbstractChargesVisitable
import com.babestudios.companyinfouk.data.model.charges.Charges
import com.babestudios.companyinfouk.data.model.charges.ChargesItem
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import com.babestudios.companyinfouk.insolvencies.ui.insolvency.list.AbstractInsolvencyVisitable

data class InsolvencyState(
		//Persons
		val insolvencyRequest: Async<Insolvency> = Uninitialized,
		val insolvencies: List<AbstractInsolvencyVisitable> = emptyList(),
		val totalChargesCount: Int = 0,
		val companyNumber: String = "",

		//Person details
		val chargesItem: ChargesItem? = null

) : MvRxState