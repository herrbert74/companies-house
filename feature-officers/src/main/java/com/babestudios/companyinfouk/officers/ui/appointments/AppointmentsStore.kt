package com.babestudios.companyinfouk.officers.ui.appointments

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.officers.Appointment
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsFragment.SideEffect
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.Intent
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.State

interface AppointmentsStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class AppointmentClicked(val appointment: Appointment) : Intent()
		data class LoadMoreAppointments(val page: Int) : Intent()
	}

	sealed class State {
		object Loading : State()

		class Show(
			val selectedOfficer: Officer,
			val appointments: List<Appointment>,
			val totalAppointmentsCount: Int
		) : State()

		class Error(val t: Throwable) : State()
	}

}
