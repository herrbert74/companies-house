package com.babestudios.companyinfouk.officers.ui.appointments

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.Intent
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.State

interface AppointmentsStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        object LoadMoreAppointments : Intent()
    }

    data class State (

        //initial data
        val selectedOfficer: Officer,

        //result data
        val appointmentsResponse: AppointmentsResponse = AppointmentsResponse(),
        val error: Throwable? = null,

        //state
        val isLoading: Boolean = true

    )

}
