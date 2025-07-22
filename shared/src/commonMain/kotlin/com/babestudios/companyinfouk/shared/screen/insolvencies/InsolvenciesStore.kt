package com.babestudios.companyinfouk.shared.screen.insolvencies

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.shared.screen.insolvencies.InsolvenciesStore.State

interface InsolvenciesStore : Store<Nothing, State, Nothing> {

	data class State(

		// initial data
		val selectedCompanyId: String,

		// result data
		val insolvency: Insolvency = Insolvency(),
		val error: Throwable? = null,

		// state
		val isLoading: Boolean = true,

		)

}
