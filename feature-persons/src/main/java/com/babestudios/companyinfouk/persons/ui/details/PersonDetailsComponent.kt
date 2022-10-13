package com.babestudios.companyinfouk.persons.ui.details

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.FlowCollector

interface PersonDetailsComp {
	sealed class Output {
		object Finished : Output()
	}
}

@Suppress("unused")
class PersonDetailsComponent(
	componentContext: ComponentContext,
	private val output: FlowCollector<PersonDetailsComp.Output>
) :PersonDetailsComp, ComponentContext by componentContext{
	//no-op
}
