package com.babestudios.companyinfouk.insolvencies.ui

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsListComp
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsListComponent
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesComp
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesComponent
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesExecutor
import com.babestudios.companyinfouk.insolvencies.ui.practitioner.PractitionerDetailsComp
import com.babestudios.companyinfouk.insolvencies.ui.practitioner.PractitionerDetailsComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.parcelize.Parcelize

internal interface InsolvenciesRootComp {
	val childStackValue: Value<ChildStack<*, InsolvenciesChild>>
}

class InsolvenciesRootComponent internal constructor(
	componentContext: ComponentContext,
	private val insolvenciesList: (ComponentContext, FlowCollector<InsolvenciesComp.Output>) -> InsolvenciesComp,
	private val insolvenciesDetails: (
		ComponentContext, selectedCompanyId: String, selectedInsolvencyCase: InsolvencyCase,
		FlowCollector<InsolvencyDetailsListComp.Output>,
	) -> InsolvencyDetailsListComp,
	private val practitionerDetails:
		(ComponentContext, selectedPractitioner: Practitioner, FlowCollector<PractitionerDetailsComp.Output>) ->
	PractitionerDetailsComp,
	private val finishHandler: () -> Unit,
) : InsolvenciesRootComp,
	ComponentContext by componentContext {

	constructor(
		componentContext: ComponentContext,
		insolvenciesExecutor: InsolvenciesExecutor,
		@MainDispatcher mainContext: CoroutineDispatcher,
		companyNumber: String,
		finishHandler: () -> Unit,
	) : this(
		componentContext = componentContext,
		insolvenciesList = { childContext, output ->
			InsolvenciesComponent(
				componentContext = childContext,
				selectedCompanyId = companyNumber,
				insolvenciesExecutor = insolvenciesExecutor,
				output = output,
			)
		},
		insolvenciesDetails = { childContext, companyId, selectedInsolvencyCase, output ->
			InsolvencyDetailsListComponent(
				componentContext = childContext,
				selectedCompanyId = companyId,
				insolvencyCase = selectedInsolvencyCase,
				mainContext = mainContext,
				output = output
			)
		},
		practitionerDetails = { childContext, selectedPractitioner, output ->
			PractitionerDetailsComponent(
				componentContext = childContext,
				selectedPractitioner = selectedPractitioner,
				mainContext = mainContext,
				output = output
			)
		},
		finishHandler,
	)

	private val navigation = StackNavigation<Configuration>()

	private val stack = childStack(
		source = navigation,
		initialStack = { listOf(Configuration.List) },
		handleBackButton = true,
		childFactory = ::createChild
	)

	override val childStackValue = stack

	private fun createChild(configuration: Configuration, componentContext: ComponentContext): InsolvenciesChild =
		when (configuration) {
			is Configuration.List -> InsolvenciesChild.List(
				insolvenciesList(componentContext, FlowCollector(::onInsolvenciesListOutput))
			)
			is Configuration.Details -> InsolvenciesChild.Details(
				insolvenciesDetails(
					componentContext, configuration.selectedCompanyId, configuration.selectedInsolvencyCase,
					FlowCollector
						(::onInsolvencyDetailsOutput)
				)
			)
			is Configuration.Practitioners -> InsolvenciesChild.Practitioners(
				practitionerDetails(
					componentContext, configuration.selectedPractitioner, FlowCollector
						(::onPractitionerDetailsOutput)
				)
			)
		}

	private fun onInsolvenciesListOutput(output: InsolvenciesComp.Output) {
		when (output) {
			is InsolvenciesComp.Output.OnInsolvencyCaseClicked ->
				navigation.push(
					Configuration.Details(
						selectedCompanyId = output.selectedCompanyId,
						selectedInsolvencyCase = output.selectedInsolvencyCase
					)
				)
			InsolvenciesComp.Output.Back -> finishHandler.invoke()
		}
	}

	private fun onInsolvencyDetailsOutput(output: InsolvencyDetailsListComp.Output): Unit =
		when (output) {
			InsolvencyDetailsListComp.Output.Back -> navigation.pop()
			is InsolvencyDetailsListComp.Output.Selected ->
				navigation.push(Configuration.Practitioners(selectedPractitioner = output.practitioner))
		}

	private fun onPractitionerDetailsOutput(output: PractitionerDetailsComp.Output): Unit =
		when (output) {
			PractitionerDetailsComp.Output.Back -> navigation.pop()
		}

}

sealed class Configuration : Parcelable {
	@Parcelize
	object List : Configuration()

	@Parcelize
	data class Details(val selectedCompanyId: String, val selectedInsolvencyCase: InsolvencyCase) : Configuration()

	@Parcelize
	data class Practitioners(val selectedPractitioner: Practitioner) : Configuration()
}

sealed class InsolvenciesChild {
	data class List(val component: InsolvenciesComp) : InsolvenciesChild()
	data class Details(val component: InsolvencyDetailsListComp) : InsolvenciesChild()
	data class Practitioners(val component: PractitionerDetailsComp) : InsolvenciesChild()
}
