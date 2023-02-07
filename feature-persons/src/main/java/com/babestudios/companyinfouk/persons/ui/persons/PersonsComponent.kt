package com.babestudios.companyinfouk.persons.ui.persons

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.common.ext.asValue
import com.babestudios.companyinfouk.domain.model.persons.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface PersonsComp {

	fun onItemClicked(person: Person)

	fun onLoadMore()

	fun onBackClicked()

	val state: Value<PersonsStore.State>

	sealed class Output {
		data class Selected(val person: Person) : Output()
		object Finish : Output()
	}

}

class PersonsComponent(
	componentContext: ComponentContext,
	val personsExecutor: PersonsExecutor,
	val companyNumber: String,
	private val output: FlowCollector<PersonsComp.Output>
) : PersonsComp, ComponentContext by componentContext {

	private var personsStore: PersonsStore =
		PersonsStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), personsExecutor).create(companyNumber)

	override fun onItemClicked(person: Person) {
		CoroutineScope(personsExecutor.mainContext).launch {
			output.emit(PersonsComp.Output.Selected(person = person))
		}
	}

	override fun onLoadMore() {
		personsStore.accept(PersonsStore.Intent.LoadMorePersons)
	}

	override fun onBackClicked() {
		CoroutineScope(personsExecutor.mainContext).launch {
			output.emit(PersonsComp.Output.Finish)
		}
	}

	override val state: Value<PersonsStore.State>
		get() = personsStore.asValue()

}
