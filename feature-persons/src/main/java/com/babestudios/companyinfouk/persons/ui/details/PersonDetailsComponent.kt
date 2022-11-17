package com.babestudios.companyinfouk.persons.ui.details

import com.arkivanov.decompose.ComponentContext
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface PersonDetailsComp {

	sealed class Output {
		object Back : Output()
		data class OnShowMapClicked(val name: String, val address: Address) : Output()
	}

	val selectedPerson: Person

	fun onBackClicked()

	fun onShowMapClicked()

}

@Suppress("unused")
class PersonDetailsComponent(
	componentContext: ComponentContext,
	@MainDispatcher
	val mainContext: CoroutineDispatcher,
	override val selectedPerson: Person,
	private val output: FlowCollector<PersonDetailsComp.Output>
) : PersonDetailsComp, ComponentContext by componentContext {

	override fun onBackClicked() {
		CoroutineScope(mainContext).launch {
			output.emit(PersonDetailsComp.Output.Back)
		}
	}

	override fun onShowMapClicked() {
		CoroutineScope(mainContext).launch {
			output.emit(PersonDetailsComp.Output.OnShowMapClicked(selectedPerson.name, selectedPerson.address))
		}
	}

}
