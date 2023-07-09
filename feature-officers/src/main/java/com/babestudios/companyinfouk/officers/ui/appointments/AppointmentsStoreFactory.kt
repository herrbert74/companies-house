package com.babestudios.companyinfouk.officers.ui.appointments

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.shared.domain.model.common.ApiResult
import com.babestudios.companyinfouk.shared.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.shared.domain.model.officers.Officer
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.Intent
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.State
import com.github.michaelbull.result.fold

class AppointmentsStoreFactory(
	private val storeFactory: StoreFactory,
	private val appointmentsExecutor: AppointmentsExecutor,
) {

	fun create(selectedOfficer: Officer): AppointmentsStore =
		object : AppointmentsStore, Store<Intent, State, Nothing> by storeFactory.create(
			name = "AppointmentsStore",
			initialState = State(selectedOfficer),
			bootstrapper = AppointmentsBootstrapper(selectedOfficer.appointmentsId),
			executorFactory = { appointmentsExecutor },
			reducer = AppointmentsReducer
		) {}

	private class AppointmentsBootstrapper(val appointmentsId: String) : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadAppointments(appointmentsId))
		}
	}

	private object AppointmentsReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.AppointmentsMessage -> msg.appointmentsResult.fold(
					success = { copy(isLoading = false, appointmentsResponse = it) },
					failure = { copy(isLoading = false, error = it) }
				)

				is Message.LoadMoreAppointmentsMessage -> msg.appointmentsResult.fold(
					success = {
						copy(
							isLoading = false,
							appointmentsResponse = AppointmentsResponse(
								items = appointmentsResponse.items.plus(it.items),
								totalResults = it.totalResults
							)
						)
					},
					failure = { copy(isLoading = false, error = it) }
				)
			}
	}

}

sealed class Message {
	data class AppointmentsMessage(val appointmentsResult: ApiResult<AppointmentsResponse>) : Message()
	data class LoadMoreAppointmentsMessage(val appointmentsResult: ApiResult<AppointmentsResponse>) : Message()
}

sealed class BootstrapIntent {
	data class LoadAppointments(val selectedOfficer: String) : BootstrapIntent()
}
