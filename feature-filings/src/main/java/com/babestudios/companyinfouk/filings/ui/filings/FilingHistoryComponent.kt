package com.babestudios.companyinfouk.filings.ui.filings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.common.ext.asValue
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface FilingHistoryComp {

	fun onItemClicked(filingHistoryItem: FilingHistoryItem)

	fun onLoadMore()

	fun onBackClicked()

	fun onFilingHistoryCategorySelected(categoryOrdinal: Int)

	val state: Value<FilingHistoryStore.State>

	sealed class Output {
		object Back : Output()
		data class Selected(val filingHistoryItem: FilingHistoryItem) : Output()
	}

	val selectedCompanyId: String

}

class FilingHistoryComponent(
	componentContext: ComponentContext,
	val filingHistoryExecutor: FilingHistoryExecutor,
	override val selectedCompanyId: String,
	private val output: FlowCollector<FilingHistoryComp.Output>,
) : FilingHistoryComp, ComponentContext by componentContext {

	private var filingHistoryStore: FilingHistoryStore =
		FilingHistoryStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), filingHistoryExecutor).create(
			selectedCompanyId
		)

	override fun onItemClicked(filingHistoryItem: FilingHistoryItem) {
		CoroutineScope(filingHistoryExecutor.mainContext).launch {
			output.emit(FilingHistoryComp.Output.Selected(filingHistoryItem = filingHistoryItem))
		}
	}

	override fun onLoadMore() {
		filingHistoryStore.accept(FilingHistoryStore.Intent.LoadMoreFilingHistory)
	}

	override fun onBackClicked() {
		CoroutineScope(filingHistoryExecutor.mainContext).launch {
			output.emit(FilingHistoryComp.Output.Back)
			filingHistoryStore.dispose()
		}
	}

	override fun onFilingHistoryCategorySelected(categoryOrdinal: Int) {
		filingHistoryStore.accept(FilingHistoryStore.Intent.FilingHistoryCategorySelected(categoryOrdinal))
	}

	override val state: Value<FilingHistoryStore.State>
		get() = filingHistoryStore.asValue()

}
