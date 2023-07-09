package com.babestudios.companyinfouk.officers.ui.officers

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.common.ext.asValue
import com.babestudios.companyinfouk.shared.domain.model.officers.Officer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface OfficersComp {

	fun onItemClicked(officer: Officer)

	fun onLoadMore()

	fun onBackClicked()

	val state: Value<OfficersStore.State>

	sealed class Output {
		data class Selected(val officer: Officer) : Output()
		object Finish : Output()
	}

}

class OfficersComponent(
	componentContext: ComponentContext,
	val officersExecutor: OfficersExecutor,
	val companyNumber: String,
	private val output: FlowCollector<OfficersComp.Output>
) : OfficersComp, ComponentContext by componentContext {

	private var officersStore: OfficersStore =
		OfficersStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), officersExecutor).create(companyNumber)

	override fun onItemClicked(officer: Officer) {
		CoroutineScope(officersExecutor.mainContext).launch {
			output.emit(OfficersComp.Output.Selected(officer = officer))
		}
	}

	override fun onLoadMore() {
		officersStore.accept(OfficersStore.Intent.LoadMoreOfficers)
	}

	override fun onBackClicked() {
		CoroutineScope(officersExecutor.mainContext).launch {
			output.emit(OfficersComp.Output.Finish)
			officersStore.dispose()
		}
	}

	override val state: Value<OfficersStore.State>
		get() = officersStore.asValue()

}
