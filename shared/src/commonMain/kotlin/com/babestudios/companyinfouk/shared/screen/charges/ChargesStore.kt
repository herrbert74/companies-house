package com.babestudios.companyinfouk.shared.screen.charges

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.shared.domain.model.charges.Charges
import com.babestudios.companyinfouk.shared.screen.charges.ChargesStore.Intent
import com.babestudios.companyinfouk.shared.screen.charges.ChargesStore.State

interface ChargesStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data object LoadMoreCharges : Intent()
    }

    data class State (

        //initial data
        val selectedCompanyId: String,

        //result data
        val chargesResponse: Charges = Charges(),
        val error: Throwable? = null,

        //state
        val isLoading: Boolean = true

    )

}
