package com.babestudios.companyinfouk.shared.screen.officerappointments

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsStore.Intent
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppointmentsExecutor constructor(
	private val companiesRepository: CompaniesRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

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
			is Intent.LoadMoreAppointments -> loadMoreAppointments(getState)
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

	private fun loadMoreAppointments(getState: () -> State) {
		val showState = getState()
		if (showState.appointmentsResponse.items.size < showState.appointmentsResponse.totalResults) {
			scope.launch {
				val appointmentsResponse = companiesRepository.getOfficerAppointments(
					showState.selectedOfficer.appointmentsId,
					(showState.appointmentsResponse.items.size).toString()
				)
				dispatch(Message.LoadMoreAppointmentsMessage(appointmentsResponse))
			}
		}
	}

	//endregion

}
