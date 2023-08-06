package com.babestudios.companyinfouk.shared.screen.company

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.shared.screen.company.CompanyStore.Intent
import com.babestudios.companyinfouk.shared.screen.company.CompanyStore.State
import com.babestudios.companyinfouk.shared.domain.model.company.Company

interface CompanyStore : Store<Intent, State, Nothing> {

	sealed class Intent {
		object FabFavouritesClicked : Intent()
	}

	data class State(

		//initial data
		val companyId: String,

		//result data
		val company: Company = Company(),
		val error: Throwable? = null,

		//state
		val isLoading: Boolean = true,
		val isFavourite: Boolean = false,

		)

}
