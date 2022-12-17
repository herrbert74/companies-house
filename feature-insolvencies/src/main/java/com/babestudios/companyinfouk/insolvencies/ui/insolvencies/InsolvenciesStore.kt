package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStore.State

interface InsolvenciesStore : Store<Nothing, State, Nothing> {

    data class State (

        //initial data
        val selectedCompanyId: String,

        //result data
        val insolvency: Insolvency = Insolvency(),
        val error: Throwable? = null,

        //state
        val isLoading: Boolean = true

    )

}
