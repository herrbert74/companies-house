package com.babestudios.companyinfouk.insolvencies.ui.details

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsStore.Intent
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsStore.State
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsVisitableBase

interface InsolvencyDetailsStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class PractitionerClicked(val selectedPractitioner: Practitioner) : Intent()
	}

	sealed class State {
		object Loading : State()

		class Show(
			val companyNumber: String,
			val insolvencyDetailVisitables: List<InsolvencyDetailsVisitableBase>,
			val datesSize: Int,
		) : State()

		class Error(val t: Throwable) : State()
	}

}
