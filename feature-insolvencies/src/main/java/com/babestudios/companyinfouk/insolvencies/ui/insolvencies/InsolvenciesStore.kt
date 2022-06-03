package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStore.Intent
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStore.State

interface InsolvenciesStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class InsolvencyClicked(val selectedInsolvency: InsolvencyCase) : Intent()
	}

	sealed class State {
		object Loading : State()

		class Show(
			val companyNumber: String,
			val insolvencies: List<InsolvencyCase>,
		) : State()

		class Error(val t: Throwable) : State()
	}

}

