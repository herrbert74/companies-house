package com.babestudios.companyinfouk.companies.ui.privacy

import com.arkivanov.decompose.ComponentContext
import com.babestudios.companyinfouk.domain.util.MainDispatcher
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
	@MainDispatcher
	private val mainContext: CoroutineDispatcher,
	private val output: FlowCollector<PrivacyComp.Output>
) : PrivacyComp, ComponentContext by componentContext {

	override fun onBackClicked() {
		CoroutineScope(mainContext).launch {
			output.emit(PrivacyComp.Output.Back)
		}
	}

}
