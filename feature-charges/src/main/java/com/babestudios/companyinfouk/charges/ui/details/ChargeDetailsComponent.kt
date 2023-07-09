package com.babestudios.companyinfouk.charges.ui.details

import com.arkivanov.decompose.ComponentContext
import com.babestudios.companyinfouk.shared.domain.model.charges.ChargesItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface ChargeDetailsComp {

	fun onBackClicked()

	sealed class Output {
		object Back : Output()
	}

	val selectedCharges: ChargesItem
}

class ChargeDetailsComponent(
	componentContext: ComponentContext,
	private val mainContext: CoroutineDispatcher,
	override val selectedCharges: ChargesItem,
	private val output: FlowCollector<ChargeDetailsComp.Output>,
) : ChargeDetailsComp, ComponentContext by componentContext {

	override fun onBackClicked() {
		CoroutineScope(mainContext).launch {
			output.emit(ChargeDetailsComp.Output.Back)
		}
	}

}
