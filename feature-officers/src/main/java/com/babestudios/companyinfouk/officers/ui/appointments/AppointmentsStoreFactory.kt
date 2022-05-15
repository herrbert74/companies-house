package com.babestudios.companyinfouk.officers.ui.appointments

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.Intent
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsFragment.SideEffect
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.State
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.State.Show
import com.babestudios.companyinfouk.officers.ui.appointments.Message.AppointmentsMessage
import com.babestudios.companyinfouk.officers.ui.appointments.Message.Error

class AppointmentsStoreFactory(
	private val storeFactory: StoreFactory,
	private val appointmentsExecutor: AppointmentsExecutor
) {

	fun create(selectedOfficer: Officer): AppointmentsStore =
		object : AppointmentsStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "AppointmentsStore",
			initialState = State.Loading,
			bootstrapper = AppointmentsBootstrapper(selectedOfficer),
			executorFactory = { appointmentsExecutor },
			reducer = AppointmentsReducer
		) {}

	private class AppointmentsBootstrapper(val selectedOfficer: Officer) : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadAppointments(selectedOfficer))
		}
	}

	private object AppointmentsReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is AppointmentsMessage -> Show(
					msg.selectedOfficer,
					msg.appointmentsResponse.items ?: emptyList(),
					msg.appointmentsResponse.totalResults
				)
				is Message.LoadMoreAppointmentsMessage -> Show(
					msg.selectedOfficer,
					(this as Show).appointments.plus(msg.appointmentsResponse.items ?: emptyList()),
					msg.appointmentsResponse.totalResults
				)
				is Error -> State.Error(msg.t)
			}
	}
}

sealed class BootstrapIntent {
	data class LoadAppointments(val selectedOfficer: Officer) : BootstrapIntent()
}

sealed class Message {
	data class AppointmentsMessage(
		val appointmentsResponse: AppointmentsResponse,
		val selectedOfficer: Officer
	) : Message()

	data class LoadMoreAppointmentsMessage(
		val appointmentsResponse: AppointmentsResponse,
		val selectedOfficer: Officer
	) : Message()

	class Error(val t: Throwable) : Message()
}
