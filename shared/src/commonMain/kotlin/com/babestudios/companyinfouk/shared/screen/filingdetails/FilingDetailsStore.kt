package com.babestudios.companyinfouk.shared.screen.filingdetails

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.shared.screen.filingdetails.FilingDetailsStore.Intent
import com.babestudios.companyinfouk.shared.screen.filingdetails.FilingDetailsStore.State
import com.eygraber.uri.Uri
import io.ktor.client.statement.HttpResponse

interface FilingDetailsStore : Store<Intent, State, Nothing> {

	sealed class Intent {
		data object FetchDocument : Intent()
		data class WriteDocument(val uri: Uri) : Intent()
	}

	data class State(

		// initial data
		val selectedFilingHistoryItem: FilingHistoryItem,
		val documentId: String = selectedFilingHistoryItem.links.documentMetadata.substringAfterLast("/"),

		// result data
		val downloadedPdfResponseBody: HttpResponse? = null,
		val savedPdfUri: Uri? = null,

		// state
		val error: Throwable? = null,

		)

}
