package com.babestudios.companyinfouk.filings.ui.details

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.common.ext.asValue
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface FilingDetailsComp {

	val state: Value<FilingDetailsStore.State>

	sealed class Output {
		object Back : Output()
	}

	val filingHistoryItem: FilingHistoryItem

	fun onBackClicked()
	fun downloadPdf()
	fun savePdf(uri: Uri)

}

@Suppress("unused")
class FilingDetailsComponent(
	componentContext: ComponentContext,
	private val filingDetailsExecutor: FilingDetailsExecutor,
	override val filingHistoryItem: FilingHistoryItem,
	private val output: FlowCollector<FilingDetailsComp.Output>,
) : FilingDetailsComp, ComponentContext by componentContext {

	private var filingDetailsStore: FilingDetailsStore =
		FilingHistoryDetailsStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), filingDetailsExecutor)
			.create(filingHistoryItem)

	override val state: Value<FilingDetailsStore.State>
		get() = filingDetailsStore.asValue()

	override fun onBackClicked() {
		CoroutineScope(filingDetailsExecutor.mainContext).launch {
			output.emit(FilingDetailsComp.Output.Back)
		}
	}

	override fun downloadPdf() {
		filingDetailsStore.accept(intent = FilingDetailsStore.Intent.FetchDocument)
	}

	override fun savePdf(uri: Uri) {
		filingDetailsStore.accept(intent = FilingDetailsStore.Intent.WriteDocument(uri))
	}

}
