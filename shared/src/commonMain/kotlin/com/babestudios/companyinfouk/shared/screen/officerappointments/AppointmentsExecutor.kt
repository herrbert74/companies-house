package com.babestudios.companyinfouk.shared.screen.officerappointments

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsStore.Intent
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppointmentsExecutor(
	private val companiesRepository: CompaniesRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

	override fun executeAction(action: BootstrapIntent) {
		when (action) {
			is BootstrapIntent.LoadAppointments -> {
				companiesRepository.logScreenView("AppointmentsFragment")
				fetchAppointments(action.selectedOfficer)
			}
		}
	}

	override fun executeIntent(intent: Intent) {
		when (intent) {
			is Intent.LoadMoreAppointments -> loadMoreAppointments(state())
		}
	}

	//region appointments actions

	private fun fetchAppointments(appointmentsId: String) {
		scope.launch(ioContext) {
			val appointmentsResponse = companiesRepository.getOfficerAppointments(appointmentsId, "0")
			withContext(mainContext) {
				dispatch(Message.AppointmentsMessage(appointmentsResponse))
			}
		}
	}

	private fun loadMoreAppointments(state: State) {
		if (state.appointmentsResponse.items.size < state.appointmentsResponse.totalResults) {
			scope.launch {
				val appointmentsResponse = companiesRepository.getOfficerAppointments(
					state.selectedOfficer.appointmentsId,
					(state.appointmentsResponse.items.size).toString()
				)
				dispatch(Message.LoadMoreAppointmentsMessage(appointmentsResponse))
			}
		}
	}

	//endregion

}
