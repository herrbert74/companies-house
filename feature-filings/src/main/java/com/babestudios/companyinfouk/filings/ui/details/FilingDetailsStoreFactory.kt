package com.babestudios.companyinfouk.filings.ui.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsStore.Intent
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsStore.State
import com.eygraber.uri.Uri
import com.eygraber.uri.toAndroidUri
import io.ktor.client.statement.HttpResponse

class FilingHistoryDetailsStoreFactory(
	private val storeFactory: StoreFactory,
	private val filingDetailsExecutor: FilingDetailsExecutor,
) {

	fun create(selectedFilingHistoryItem: FilingHistoryItem): FilingDetailsStore =
		object : FilingDetailsStore, Store<Intent, State, Nothing> by storeFactory.create(
			name = "FilingHistoryDetailsStore",
			initialState = State(selectedFilingHistoryItem),
			executorFactory = { filingDetailsExecutor },
			reducer = FilingHistoryDetailsReducer
		) {}

	private object FilingHistoryDetailsReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.DocumentDownloaded -> copy(downloadedPdfResponseBody = msg.documentResponse)
				is Message.DocumentWritten -> copy(savedPdfUri = msg.uri.toAndroidUri())
			}
	}
}

sealed class Message {
	data class DocumentDownloaded(val documentResponse: HttpResponse) : Message()
	data class DocumentWritten(val uri: Uri) : Message()
}
