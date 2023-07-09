package com.babestudios.companyinfouk.officers.ui.officers

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.Intent
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.State

interface OfficersStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        object LoadMoreOfficers : Intent()
    }

    data class State (

        //initial data
        val companyId: String,

        //result data
        val officersResponse: OfficersResponse = OfficersResponse(),
        val error: Throwable? = null,

        //state
        val isLoading: Boolean = true

    )

}
