package com.babestudios.companyinfouk.officers.ui.appointments

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.data.BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.officers.Appointment
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsFragment.SideEffect
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.Intent
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class AppointmentsExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadAppointments -> {
				companiesRepository.logScreenView("AppointmentsFragment")
				fetchAppointments(action.selectedOfficer)
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.AppointmentClicked -> appointmentClicked(intent.appointment)
			is Intent.LoadMoreAppointments -> loadMoreAppointments(getState, intent.page)
		}
	}

	private fun appointmentClicked(appointment: Appointment) {
		publish(
			SideEffect.AppointmentClicked(
				appointment.appointedTo.companyNumber, appointment.appointedTo.companyName
			)
		)
	}

	private fun fetchAppointments(selectedOfficer: Officer) {
		scope.launch {
			val response = companiesRepository.getOfficerAppointments(selectedOfficer.appointmentsId, "0")
			dispatch(Message.AppointmentsMessage(response, selectedOfficer))
		}
	}

	private fun loadMoreAppointments(getState: () -> State, page: Int) {
		val showState = (getState() as State.Show)
		if (showState.appointments.size < showState.totalAppointmentsCount) {
			scope.launch {
				val response = companiesRepository.getOfficerAppointments(
					showState.selectedOfficer.appointmentsId,
					(page * Integer.valueOf(COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString()
				)
				dispatch(Message.LoadMoreAppointmentsMessage(response, showState.selectedOfficer))
			}
		}
	}
}
