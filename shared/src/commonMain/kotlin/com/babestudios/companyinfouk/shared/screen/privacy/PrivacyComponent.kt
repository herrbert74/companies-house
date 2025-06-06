package com.babestudios.companyinfouk.shared.screen.privacy

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface PrivacyComp {

	sealed class Output {
		object Back : Output()
	}

	fun onBackClicked()

}

@Suppress("unused")
class PrivacyComponent(
	componentContext: ComponentContext,
	private val mainContext: CoroutineDispatcher,
	private val output: FlowCollector<PrivacyComp.Output>
) : PrivacyComp, ComponentContext by componentContext {

	override fun onBackClicked() {
		CoroutineScope(mainContext).launch {
			output.emit(PrivacyComp.Output.Back)
		}
	}

}
