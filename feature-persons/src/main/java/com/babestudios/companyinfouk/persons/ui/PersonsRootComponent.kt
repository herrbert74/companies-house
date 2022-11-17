package com.babestudios.companyinfouk.persons.ui

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.persons.ui.details.PersonDetailsComp
import com.babestudios.companyinfouk.persons.ui.details.PersonDetailsComponent
import com.babestudios.companyinfouk.persons.ui.persons.PersonsExecutor
import com.babestudios.companyinfouk.persons.ui.persons.PersonsListComp
import com.babestudios.companyinfouk.persons.ui.persons.PersonsListComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.parcelize.Parcelize

internal interface PersonsRootComp {
	val childStackValue: Value<ChildStack<*, PersonsChild>>
}

class PersonsRootComponent internal constructor(
	componentContext: ComponentContext,
	private val personsList: (ComponentContext, FlowCollector<PersonsListComp.Output>) -> PersonsListComp,
	private val personDetails: (ComponentContext, selectedPerson: Person, FlowCollector<PersonDetailsComp.Output>) ->
	PersonDetailsComp,
	val companyNumber: String,
	private val finishHandler: () -> Unit,
	private val showOnMapHandler: (String, Address) -> Unit,
) : PersonsRootComp,
	ComponentContext by componentContext {

	constructor(
		componentContext: ComponentContext,
		personsExecutor: PersonsExecutor,
		@MainDispatcher mainContext: CoroutineDispatcher,
		companyNumber: String,
		finishHandler: () -> Unit,
		showOnMapHandler: (String, Address) -> Unit,
	) : this(
		componentContext = componentContext,
		personsList = { childContext, output ->
			PersonsListComponent(
				componentContext = childContext,
				companyNumber = companyNumber,
				personsExecutor = personsExecutor,
				output = output,
			)
		},
		personDetails = { childContext, selectedPerson, output ->
			PersonDetailsComponent(
				componentContext = childContext,
				selectedPerson = selectedPerson,
				mainContext = mainContext,
				output = output
			)
		},
		companyNumber,
		finishHandler,
		showOnMapHandler,
	)

	private val navigation = StackNavigation<Configuration>()

	private val stack = childStack(
		source = navigation,
		initialStack = { listOf(Configuration.List) },
		handleBackButton = true,
		childFactory = ::createChild
	)

	override val childStackValue = stack

	private fun createChild(configuration: Configuration, componentContext: ComponentContext): PersonsChild =
		when (configuration) {
			is Configuration.List -> PersonsChild.List(
				personsList(componentContext, FlowCollector(::onPersonsListOutput))
			)
			is Configuration.Details -> PersonsChild.Details(
				personDetails(componentContext, configuration.selectedPerson, FlowCollector(::onPersonDetailsOutput))
			)
		}

	private fun onPersonsListOutput(output: PersonsListComp.Output) {
		when (output) {
			is PersonsListComp.Output.Selected -> navigation.push(Configuration.Details(selectedPerson = output.person))
			PersonsListComp.Output.Finish -> finishHandler.invoke()
		}
	}

	private fun onPersonDetailsOutput(output: PersonDetailsComp.Output): Unit =
		when (output) {
			is PersonDetailsComp.Output.Back -> navigation.pop()
			is PersonDetailsComp.Output.OnShowMapClicked -> showOnMapHandler(output.name, output.address)
		}

}

sealed class Configuration : Parcelable {
	@Parcelize
	object List : Configuration()

	@Parcelize
	data class Details(val selectedPerson: Person) : Configuration()
}

sealed class PersonsChild {
	data class List(val component: PersonsListComp) : PersonsChild()
	data class Details(val component: PersonDetailsComp) : PersonsChild()
}
