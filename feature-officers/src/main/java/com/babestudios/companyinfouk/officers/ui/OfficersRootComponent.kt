package com.babestudios.companyinfouk.officers.ui

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsExecutor
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsListComp
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsListComponent
import com.babestudios.companyinfouk.officers.ui.details.OfficerDetailsComp
import com.babestudios.companyinfouk.officers.ui.details.OfficerDetailsComponent
import com.babestudios.companyinfouk.officers.ui.officers.OfficersExecutor
import com.babestudios.companyinfouk.officers.ui.officers.OfficersListComp
import com.babestudios.companyinfouk.officers.ui.officers.OfficersListComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.parcelize.Parcelize

internal interface OfficersRootComp {
	val childStackValue: Value<ChildStack<*, OfficersChild>>
}

class OfficersRootComponent internal constructor(
	componentContext: ComponentContext,
	private val officersList: (ComponentContext, FlowCollector<OfficersListComp.Output>) -> OfficersListComp,
	private val officerDetails:
		(ComponentContext, selectedOfficer: Officer, FlowCollector<OfficerDetailsComp.Output>) -> OfficerDetailsComp,
	private val officerAppointments:
		(
		ComponentContext,
		selectedOfficer: Officer,
		FlowCollector<AppointmentsListComp.Output>,
	) -> AppointmentsListComp,
	private val finishHandler: () -> Unit,
	private val showOnMapHandler: (String, Address) -> Unit,
	private val navigateToCompany: (String, String) -> Unit,
) : OfficersRootComp,
	ComponentContext by componentContext {

	constructor(
		componentContext: ComponentContext,
		officersExecutor: OfficersExecutor,
		companiesRepository: CompaniesRepository,
		@MainDispatcher mainContext: CoroutineDispatcher,
		@IoDispatcher ioContext: CoroutineDispatcher,
		companyNumber: String,
		finishHandler: () -> Unit,
		showOnMapHandler: (String, Address) -> Unit,
		navigateToCompany: (String, String) -> Unit,
	) : this(
		componentContext = componentContext,
		officersList = { childContext, output ->
			OfficersListComponent(
				componentContext = childContext,
				companyNumber = companyNumber,
				officersExecutor = officersExecutor,
				output = output,
			)
		},
		officerDetails = { childContext, selectedOfficer, output ->
			OfficerDetailsComponent(
				componentContext = childContext,
				mainContext = mainContext,
				companyNumber,
				selectedOfficer = selectedOfficer,
				output = output
			)
		},
		officerAppointments = { childContext, selectedOfficer, output ->
			AppointmentsListComponent(
				componentContext = childContext,
				AppointmentsExecutor(companiesRepository, mainContext, ioContext),
				selectedOfficer = selectedOfficer,
				output = output
			)
		},
		finishHandler,
		showOnMapHandler,
		navigateToCompany,
	)

	private val navigation = StackNavigation<Configuration>()

	private val stack = childStack(
		source = navigation,
		initialStack = { listOf(Configuration.List) },
		handleBackButton = true,
		childFactory = ::createChild
	)

	override val childStackValue = stack

	private fun createChild(configuration: Configuration, componentContext: ComponentContext): OfficersChild =
		when (configuration) {
			is Configuration.List -> OfficersChild.List(
				officersList(componentContext, FlowCollector(::onOfficersListOutput))
			)
			is Configuration.Details -> OfficersChild.Details(
				officerDetails(componentContext, configuration.selectedOfficer, FlowCollector(::onOfficerDetailsOutput))
			)
			is Configuration.Appointments -> OfficersChild.Appointments(
				officerAppointments(
					componentContext,
					configuration.selectedOfficer,
					FlowCollector(::onOfficerAppointmentsOutput)
				)
			)
		}

	private fun onOfficersListOutput(output: OfficersListComp.Output) {
		when (output) {
			is OfficersListComp.Output.Selected -> navigation.push(
				Configuration.Details(selectedOfficer = output.officer)
			)
			OfficersListComp.Output.Finish -> finishHandler.invoke()
		}
	}

	private fun onOfficerDetailsOutput(output: OfficerDetailsComp.Output): Unit =
		when (output) {
			is OfficerDetailsComp.Output.Back -> navigation.pop()
			is OfficerDetailsComp.Output.OnShowMapClicked -> showOnMapHandler(output.name, output.address)
			is OfficerDetailsComp.Output.OnAppointmentsClicked -> navigation.push(
				Configuration.Appointments(selectedOfficer = output.selectedOfficer)
			)
		}

	private fun onOfficerAppointmentsOutput(output: AppointmentsListComp.Output): Unit =
		when (output) {
			AppointmentsListComp.Output.Back -> navigation.pop()
			is AppointmentsListComp.Output.Selected -> navigateToCompany(
				output.appointment.appointedTo.companyNumber,
				output.appointment.appointedTo.companyName
			)
		}

}

sealed class Configuration : Parcelable {
	@Parcelize
	object List : Configuration()

	@Parcelize
	data class Details(val selectedOfficer: Officer) : Configuration()

	@Parcelize
	data class Appointments(val selectedOfficer: Officer) : Configuration()
}

sealed class OfficersChild {
	data class List(val component: OfficersListComp) : OfficersChild()
	data class Details(val component: OfficerDetailsComp) : OfficersChild()
	data class Appointments(val component: AppointmentsListComp) : OfficersChild()
}
