package com.babestudios.companyinfouk.officers.ui.officers

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.Intent
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.State

interface OfficersStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class OfficerItemClicked(val selectedOfficer: Officer) : Intent()
		data class LoadMoreOfficers(val page: Int) : Intent()
	}

	sealed class State {

		object Loading : State()

		class Show(
			val companyNumber: String,
			val officers: List<Officer>,
			val totalOfficersCount: Int
		) : State()

		class Error(val t: Throwable) : State()

	}

}
