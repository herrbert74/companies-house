package com.babestudios.companyinfouk.companies.ui.company

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore.Intent
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore.State
import com.babestudios.companyinfouk.domain.model.company.Company

interface CompanyStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		object FabFavouritesClicked : Intent()
		object MapClicked : Intent()
	}

	sealed class State {
		object Loading : State()

		class Show(
			val companyNumber: String,
			val company: Company,
			val isFavourite: Boolean,
		) : State()

		class Error(val t: Throwable) : State()
	}

}
