package com.babestudios.companyinfouk.shared.screen.officerappointments

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.shared.ext.asValue
import com.babestudios.companyinfouk.shared.domain.model.officers.Appointment
import com.babestudios.companyinfouk.shared.domain.model.officers.Officer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface AppointmentsComp {

	fun onItemClicked(appointment: Appointment)

	fun onLoadMore()

	fun onBackClicked()

	val state: Value<AppointmentsStore.State>

	sealed class Output {
		data class Selected(val appointment: Appointment) : Output()
		object Back : Output()
	}

}

class AppointmentsComponent(
	componentContext: ComponentContext,
	private val appointmentsExecutor: AppointmentsExecutor,
	val selectedOfficer: Officer,
	private val output: FlowCollector<AppointmentsComp.Output>,
) : AppointmentsComp, ComponentContext by componentContext {

	private var appointmentsStore: AppointmentsStore =
		AppointmentsStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), appointmentsExecutor)
			.create(selectedOfficer)

	override fun onItemClicked(appointment: Appointment) {
		CoroutineScope(appointmentsExecutor.mainContext).launch {
			output.emit(AppointmentsComp.Output.Selected(appointment = appointment))
		}
	}

	override fun onLoadMore() {
		appointmentsStore.accept(AppointmentsStore.Intent.LoadMoreAppointments)
	}

	override fun onBackClicked() {
		CoroutineScope(appointmentsExecutor.mainContext).launch {
			output.emit(AppointmentsComp.Output.Back)
			appointmentsStore.dispose()
		}
	}

	override val state: Value<AppointmentsStore.State>
		get() = appointmentsStore.asValue()

}
