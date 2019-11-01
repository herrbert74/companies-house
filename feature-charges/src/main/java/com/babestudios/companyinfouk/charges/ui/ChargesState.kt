package com.babestudios.companyinfouk.charges.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.charges.ui.charges.list.AbstractChargesVisitable
import com.babestudios.companyinfouk.data.model.charges.Charges
import com.babestudios.companyinfouk.data.model.charges.ChargesItem

data class ChargesState(
		//Persons
		val chargesRequest: Async<Charges> = Uninitialized,
		val charges: List<AbstractChargesVisitable> = emptyList(),
		val totalChargesCount: Int = 0,
		val companyNumber: String = "",

		//Person details
		val chargesItem: ChargesItem? = null

) : MvRxState