package com.babestudios.companyinfouk.officers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.events
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsExecutor
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsFragment
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStoreFactory
import com.babestudios.companyinfouk.officers.ui.officers.OfficersExecutor
import com.babestudios.companyinfouk.officers.ui.officers.OfficersFragment
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.Intent
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStoreFactory
import com.babestudios.companyinfouk.officers.ui.officers.UserIntent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map

class OfficersViewModel @AssistedInject constructor(
	officersExecutor: OfficersExecutor,
	private val appointmentsExecutor: AppointmentsExecutor,
	@Assisted val companyNumber: String,
) : ViewModel() {

	companion object {

		fun provideFactory(
			assistedFactory: OfficersViewModelFactory,
			companyNumber: String
		): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
			@Suppress("UNCHECKED_CAST")
			override fun <T : ViewModel> create(modelClass: Class<T>): T {
				return assistedFactory.create(companyNumber) as T
			}
		}
	}

	private var officersStore: OfficersStore =
		OfficersStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), officersExecutor).create(companyNumber)

	private var appointmentsStore: AppointmentsStore? = null

	fun onViewCreated(
		view: OfficersFragment,
		lifecycle: Lifecycle
	) {

		bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
			officersStore.states bindTo view
			officersStore.labels bindTo { view.sideEffects(it) }
			view.events.map { userIntentToIntent(it) } bindTo officersStore
		}
	}

	fun onViewCreated(
		view: AppointmentsFragment,
		lifecycle: Lifecycle,
		selectedOfficer: Officer
	) {
		if (appointmentsStore == null) {
			appointmentsStore = AppointmentsStoreFactory(
				LoggingStoreFactory(DefaultStoreFactory()),
				appointmentsExecutor
			).create(selectedOfficer)
		}

		bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
			appointmentsStore?.states!! bindTo view
			appointmentsStore?.labels!! bindTo { view.sideEffects(it) }
			view.events.map { appointmentUserIntentToIntent(it) } bindTo appointmentsStore!!
		}
	}

	private val userIntentToIntent: UserIntent.() -> Intent =
		{
			when (this) {
				is UserIntent.OfficerItemClicked -> Intent.OfficerItemClicked(selectedOfficer)
				is UserIntent.LoadMoreOfficers -> Intent.LoadMoreOfficers(page)
			}
		}

	private val appointmentUserIntentToIntent: AppointmentsFragment.UserIntent.() -> AppointmentsStore.Intent =
		{
			when (this) {
				is AppointmentsFragment.UserIntent.AppointmentClicked ->
					AppointmentsStore.Intent.AppointmentClicked(selectedAppointment)
				is AppointmentsFragment.UserIntent.LoadMoreAppointments ->
					AppointmentsStore.Intent.LoadMoreAppointments(page)
			}
		}

	override fun onCleared() {
		officersStore::dispose
		appointmentsStore?.dispose()
	}

	//endregion

}

@AssistedFactory
interface OfficersViewModelFactory {
	fun create(companyNumber: String): OfficersViewModel
}
