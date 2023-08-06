package com.babestudios.companyinfouk.shared.screen.insolvencies

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.shared.ext.asValue
import com.babestudios.companyinfouk.shared.domain.model.insolvency.InsolvencyCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface InsolvenciesComp {

	val state: Value<InsolvenciesStore.State>

	sealed class Output {
		object Back : Output()
		data class OnInsolvencyCaseClicked(
			val selectedCompanyId: String,
			val selectedInsolvencyCase: InsolvencyCase,
		) : Output()
	}

	val selectedCompanyId: String

	fun onBackClicked()

	fun onInsolvencyCaseClicked(selectedInsolvencyCase: InsolvencyCase)

}

@Suppress("unused")
class InsolvenciesComponent(
	componentContext: ComponentContext,
	private val insolvenciesExecutor: InsolvenciesExecutor,
	override val selectedCompanyId: String,
	private val output: FlowCollector<InsolvenciesComp.Output>,
) : InsolvenciesComp, ComponentContext by componentContext {

	private var insolvenciesStore: InsolvenciesStore =
		InsolvenciesStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), insolvenciesExecutor)
			.create(selectedCompanyId)

	override fun onBackClicked() {
		CoroutineScope(insolvenciesExecutor.mainContext).launch {
			output.emit(InsolvenciesComp.Output.Back)
		}
	}

	override fun onInsolvencyCaseClicked(selectedInsolvencyCase: InsolvencyCase) {
		CoroutineScope(insolvenciesExecutor.mainContext).launch {
			output.emit(InsolvenciesComp.Output.OnInsolvencyCaseClicked(selectedCompanyId, selectedInsolvencyCase))
		}
	}

	override val state: Value<InsolvenciesStore.State>
		get() = insolvenciesStore.asValue()

}
