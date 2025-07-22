package com.babestudios.companyinfouk.shared.screen.officerappointments

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.shared.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.shared.domain.model.officers.Officer
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsStore.Intent
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsStore.State

interface AppointmentsStore : Store<Intent, State, Nothing> {

	sealed class Intent {
		object LoadMoreAppointments : Intent()
	}

	data class State(

		// initial data
		val selectedOfficer: Officer,

		// result data
		val appointmentsResponse: AppointmentsResponse = AppointmentsResponse(),
		val error: Throwable? = null,

		// state
		val isLoading: Boolean = true,

		)

}
