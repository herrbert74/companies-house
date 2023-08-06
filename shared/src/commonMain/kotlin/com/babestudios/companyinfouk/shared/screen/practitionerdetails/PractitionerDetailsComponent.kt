package com.babestudios.companyinfouk.shared.screen.practitionerdetails

import com.arkivanov.decompose.ComponentContext
import com.babestudios.companyinfouk.shared.domain.model.common.getAddressString
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Practitioner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface PractitionerDetailsComp {

	sealed class Output {
		object Back : Output()
		data class OnShowMapClicked(val name: String, val address: String) : Output()
	}

	val selectedPractitioner: Practitioner

	fun onBackClicked()

	fun onShowMapClicked()

}

@Suppress("unused")
class PractitionerDetailsComponent(
	componentContext: ComponentContext,
	val mainContext: CoroutineDispatcher,
	override val selectedPractitioner: Practitioner,
	private val output: FlowCollector<PractitionerDetailsComp.Output>,
) : PractitionerDetailsComp, ComponentContext by componentContext {

	override fun onBackClicked() {
		CoroutineScope(mainContext).launch {
			output.emit(PractitionerDetailsComp.Output.Back)
		}
	}

	override fun onShowMapClicked() {
		CoroutineScope(mainContext).launch {
			output.emit(
				PractitionerDetailsComp.Output.OnShowMapClicked(
					selectedPractitioner.name,
					selectedPractitioner.address.getAddressString()
				)
			)
		}
	}

}
