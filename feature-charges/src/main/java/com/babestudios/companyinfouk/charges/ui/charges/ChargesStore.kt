package com.babestudios.companyinfouk.charges.ui.charges

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore.Intent
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore.State
import com.babestudios.companyinfouk.domain.model.charges.Charges
import com.babestudios.companyinfouk.domain.model.charges.ChargesItem

interface ChargesStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class ChargesItemClicked(val selectedChargesItem: ChargesItem) : Intent()
		data class LoadMoreCharges(val page: Int) : Intent()
	}

	sealed class State {

		object Loading : State()

		class Show(
			val companyNumber: String,
			val charges: Charges,
		) : State()

		class Error(val t: Throwable) : State()

	}

}

