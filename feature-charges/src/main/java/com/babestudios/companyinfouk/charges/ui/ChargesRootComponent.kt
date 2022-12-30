package com.babestudios.companyinfouk.charges.ui

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.charges.ui.details.ChargeDetailsListComp
import com.babestudios.companyinfouk.charges.ui.details.ChargeDetailsListComponent
import com.babestudios.companyinfouk.charges.ui.charges.ChargesExecutor
import com.babestudios.companyinfouk.charges.ui.charges.ChargesListComp
import com.babestudios.companyinfouk.charges.ui.charges.ChargesListComponent
import com.babestudios.companyinfouk.domain.model.charges.ChargesItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.parcelize.Parcelize

internal interface ChargesRootComp {
	val childStackValue: Value<ChildStack<*, ChargesChild>>
}

class ChargesRootComponent internal constructor(
	componentContext: ComponentContext,
	private val chargesList: (ComponentContext, FlowCollector<ChargesListComp.Output>) -> ChargesListComp,
	private val chargeDetails:
		(ComponentContext, selectedCharges: ChargesItem, FlowCollector<ChargeDetailsListComp.Output>) ->
	ChargeDetailsListComp,
	private val finishHandler: () -> Unit,
) : ChargesRootComp,
	ComponentContext by componentContext {

	constructor(
		componentContext: ComponentContext,
		chargesExecutor: ChargesExecutor,
		@MainDispatcher mainContext: CoroutineDispatcher,
		companyNumber: String,
		finishHandler: () -> Unit,
	) : this(
		componentContext = componentContext,
		chargesList = { childContext, output ->
			ChargesListComponent(
				componentContext = childContext,
				companyNumber = companyNumber,
				chargesExecutor = chargesExecutor,
				output = output,
			)
		},
		chargeDetails = { childContext, selectedCharges, output ->
			ChargeDetailsListComponent(
				componentContext = childContext,
				selectedCharges = selectedCharges,
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

	private fun createChild(configuration: Configuration, componentContext: ComponentContext): ChargesChild =
		when (configuration) {
			is Configuration.List -> ChargesChild.List(
				chargesList(componentContext, FlowCollector(::onChargesListOutput))
			)

			is Configuration.Details -> ChargesChild.Details(
				chargeDetails(componentContext, configuration.selectedCharges, FlowCollector(::onChargesDetailsOutput))
			)
		}

	private fun onChargesListOutput(output: ChargesListComp.Output) = when (output) {
		is ChargesListComp.Output.Selected ->
			navigation.push(Configuration.Details(selectedCharges = output.chargesItem))

		ChargesListComp.Output.Back -> finishHandler.invoke()
	}

	private fun onChargesDetailsOutput(output: ChargeDetailsListComp.Output) = when (output) {
		ChargeDetailsListComp.Output.Back -> navigation.pop()
	}

}

sealed class Configuration : Parcelable {
	@Parcelize
	object List : Configuration()

	@Parcelize
	data class Details(val selectedCharges: ChargesItem) : Configuration()
}

sealed class ChargesChild {
	data class List(val component: ChargesListComp) : ChargesChild()
	data class Details(val component: ChargeDetailsListComp) : ChargesChild()
}
