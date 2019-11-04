package com.babestudios.companyinfouk.insolvencies.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.ui.details.list.AbstractInsolvencyDetailsVisitable
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.AbstractInsolvencyVisitable

data class InsolvenciesState(
		//Insolvency
		val insolvencyRequest: Async<Insolvency> = Uninitialized,
		val insolvencies: List<AbstractInsolvencyVisitable> = emptyList(),
		val totalInsolvenciesCount: Int = 0,
		val companyNumber: String = "",

		//Insolvency case
		val insolvencyCase: InsolvencyCase = InsolvencyCase(),
		val insolvencyDetailsItems: List<AbstractInsolvencyDetailsVisitable> = emptyList()
) : MvRxState