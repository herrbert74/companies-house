package com.babestudios.companyinfouk.officers.ui.officers

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.Intent
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.State
import com.github.michaelbull.result.Result

interface OfficersStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class OfficerItemClicked(val selectedOfficer: Officer) : Intent()
		data class LoadMoreOfficers(val page: Int) : Intent()
	}

	sealed class State {

		object Loading : State()

		class Show(
			val companyNumber: String,
			val officersResponse: OfficersResponse,
		) : State()

		class Error(val t: Throwable) : State()

	}

}
