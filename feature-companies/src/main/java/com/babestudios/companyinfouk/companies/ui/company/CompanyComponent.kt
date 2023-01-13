package com.babestudios.companyinfouk.companies.ui.company

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.common.ext.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface CompanyComp {

	sealed class Output {
		object Back : Output()
		data class Selected(val name: String) : Output()
	}

	val companyName: String

	val companyId: String

	fun onBackClicked()

	val state: Value<CompanyStore.State>

	fun onTemplatePlaceHolderClicked()

}

@Suppress("unused")
class CompanyComponent(
	componentContext: ComponentContext,
	val companyExecutor: CompanyExecutor,
	override val companyName: String,
	override val companyId: String,
	private val output: FlowCollector<CompanyComp.Output>,
) : CompanyComp, ComponentContext by componentContext {

	private var companyStore =
		CompanyStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), companyExecutor).create(companyId)

	override fun onBackClicked() {
		CoroutineScope(companyExecutor.mainContext).launch {
			output.emit(CompanyComp.Output.Back)
		}
	}

	override fun onTemplatePlaceHolderClicked() {
//		CoroutineScope(companyExecutor.mainContext).launch {
//			output.emit(CompanyComp.Output.Selected(companyId.name))
//		}
	}

	override val state: Value<CompanyStore.State>
		get() = companyStore.asValue()

}
