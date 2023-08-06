package com.babestudios.companyinfouk.shared.screen.insolvencydetails

import com.arkivanov.decompose.ComponentContext
import com.babestudios.companyinfouk.shared.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Practitioner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface InsolvencyDetailsComp {

	fun onItemClicked(practitioner: Practitioner)

	fun onBackClicked()

	sealed class Output {
		data class Selected(val practitioner: Practitioner) : Output()
		object Back : Output()
	}

	val selectedCompanyId: String

	val insolvencyCase: InsolvencyCase

}

class InsolvencyDetailsComponent(
	componentContext: ComponentContext,
	val mainContext: CoroutineDispatcher,
	override val selectedCompanyId: String,
	override val insolvencyCase: InsolvencyCase,
	private val output: FlowCollector<InsolvencyDetailsComp.Output>,
) : InsolvencyDetailsComp, ComponentContext by componentContext {

	override fun onItemClicked(practitioner: Practitioner) {
		CoroutineScope(mainContext).launch {
			output.emit(InsolvencyDetailsComp.Output.Selected(practitioner))
		}
	}

	override fun onBackClicked() {
		CoroutineScope(mainContext).launch {
			output.emit(InsolvencyDetailsComp.Output.Back)
		}
	}

}
