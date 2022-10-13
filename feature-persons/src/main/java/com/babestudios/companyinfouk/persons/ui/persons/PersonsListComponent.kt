package com.babestudios.companyinfouk.persons.ui.persons

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.babestudios.companyinfouk.domain.model.persons.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface PersonsListComp {

	fun onItemClicked(person: Person)

	val state: Value<PersonsStore.State>

	sealed class Output {
		data class Selected(val person: Person) : Output()
	}

}

class PersonsListComponent(
	componentContext: ComponentContext,
	val personsExecutor: PersonsExecutor,
	//storeFactory: StoreFactory,
	private val output: FlowCollector<PersonsListComp.Output>
) : PersonsListComp, ComponentContext by componentContext {

	override fun onItemClicked(person: Person) {
		CoroutineScope(personsExecutor.mainContext).launch {
			output.emit(PersonsListComp.Output.Selected(person = person))
		}
	}

	override val state: Value<PersonsStore.State>
		get() = TODO("Not yet implemented")

}
