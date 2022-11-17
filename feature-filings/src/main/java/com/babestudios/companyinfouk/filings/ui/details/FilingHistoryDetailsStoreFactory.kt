package com.babestudios.companyinfouk.filings.ui.details

import android.net.Uri
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsStore.Intent
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsStore.State
import okhttp3.ResponseBody

class FilingHistoryDetailsStoreFactory(
	private val storeFactory: StoreFactory,
	private val filingHistoryDetailsExecutor: FilingHistoryDetailsExecutor
) {

	fun create(
		companyNumber: String,
		selectedFilingHistoryItem: FilingHistoryItem,
	): FilingHistoryDetailsStore =
		object : FilingHistoryDetailsStore, Store<Intent, State, Nothing> by storeFactory.create(
			name = "FilingHistoryDetailsStore",
			initialState = State.Show(companyNumber, selectedFilingHistoryItem),
			executorFactory = { filingHistoryDetailsExecutor },
			reducer = FilingHistoryDetailsReducer
		) {}

	private object FilingHistoryDetailsReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.DocumentDownloaded -> {
					val state = (this as State.Show)
					State.DocumentDownloaded(
						state.companyNumber, state.selectedFilingHistoryItem, responseBody = msg.responseBody
					)
				}
				is Message.DocumentWritten -> {
					val state = (this as State.DocumentDownloaded)
					State.DocumentWritten(
						state.companyNumber, state.selectedFilingHistoryItem, uri = msg.uri
					)
				}
			}
	}
}

sealed class Message {
	data class DocumentDownloaded(val responseBody: ResponseBody) : Message()
	data class DocumentWritten(val uri: Uri) : Message()
}
