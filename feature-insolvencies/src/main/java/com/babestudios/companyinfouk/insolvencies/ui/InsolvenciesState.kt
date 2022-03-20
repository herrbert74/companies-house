package com.babestudios.companyinfouk.insolvencies.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsVisitableBase
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvencyVisitableBase

data class InsolvenciesState(
		//Insolvency
	val insolvencyRequest: Async<Insolvency> = Uninitialized,
	val insolvencies: List<InsolvencyVisitableBase> = emptyList(),
	val totalInsolvenciesCount: Int = 0,
	@PersistState
		val companyNumber: String = "",

		//Insolvency case
	@PersistState
		val insolvencyCase: InsolvencyCase = InsolvencyCase(),
	val insolvencyDetailsItems: List<InsolvencyDetailsVisitableBase> = emptyList(),

		//Practitioner
	@PersistState
		val selectedPractitioner: Practitioner? = null,
) : MvRxState
