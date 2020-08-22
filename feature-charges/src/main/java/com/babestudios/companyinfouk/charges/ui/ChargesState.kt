package com.babestudios.companyinfouk.charges.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.charges.ui.charges.list.ChargesVisitableBase
import com.babestudios.companyinfouk.common.model.charges.Charges
import com.babestudios.companyinfouk.common.model.charges.ChargesItem

data class ChargesState(
		//Charges
		val chargesRequest: Async<Charges> = Uninitialized,
		val charges: List<ChargesVisitableBase> = emptyList(),
		val totalChargesCount: Int = 0,
		@PersistState
		val companyNumber: String = "",

		//Charge details
		@PersistState
		val chargesItem: ChargesItem? = null

) : MvRxState
