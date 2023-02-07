package com.babestudios.companyinfouk.officers.ui.details

import com.arkivanov.decompose.ComponentContext
import com.babestudios.companyinfouk.domain.model.common.getAddressString
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface OfficerDetailsComp {

	sealed class Output {
		object Back : Output()
		data class OnShowMapClicked(val name: String, val address: String) : Output()
		data class OnAppointmentsClicked(val selectedOfficer: Officer) : Output()
	}

	//val selectedCompanyId: String

	val selectedOfficer: Officer

	fun onBackClicked()

	fun onShowMapClicked()

	fun onAppointmentsClicked()

}

@Suppress("unused")
class OfficerDetailsComponent(
	componentContext: ComponentContext,
	@MainDispatcher
	val mainContext: CoroutineDispatcher,
	override val selectedOfficer: Officer,
	private val output: FlowCollector<OfficerDetailsComp.Output>,
) : OfficerDetailsComp, ComponentContext by componentContext {

	override fun onBackClicked() {
		CoroutineScope(mainContext).launch {
			output.emit(OfficerDetailsComp.Output.Back)
		}
	}

	override fun onShowMapClicked() {
		CoroutineScope(mainContext).launch {
			output.emit(
				OfficerDetailsComp.Output.OnShowMapClicked(
					selectedOfficer.name,
					selectedOfficer.address.getAddressString()
				)
			)
		}
	}

	override fun onAppointmentsClicked() {
		CoroutineScope(mainContext).launch {
			output.emit(OfficerDetailsComp.Output.OnAppointmentsClicked(selectedOfficer))
		}
	}

}
