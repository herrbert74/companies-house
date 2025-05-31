package com.babestudios.companyinfouk.shared.screen.map

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface MapComp {

	sealed class Output {
		data object Back : Output()
	}

	val name: String

	val address: String

	fun onBackClicked()

}

@Suppress("unused")
class MapComponent(
	componentContext: ComponentContext,
	private val mainContext: CoroutineDispatcher,
	override val name: String,
	override val address: String,
	private val output: FlowCollector<MapComp.Output>,
) : MapComp, ComponentContext by componentContext {

	override fun onBackClicked() {
		CoroutineScope(mainContext).launch {
			output.emit(MapComp.Output.Back)
		}
	}

}
