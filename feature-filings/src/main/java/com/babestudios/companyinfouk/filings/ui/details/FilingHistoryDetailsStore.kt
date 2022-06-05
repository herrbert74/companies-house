package com.babestudios.companyinfouk.filings.ui.details

import android.net.Uri
import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsStore.Intent
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsStore.State
import okhttp3.ResponseBody

interface FilingHistoryDetailsStore : Store<Intent, State, Nothing> {

	sealed class Intent {
		data class FetchDocument(val documentId: String) : Intent()
		data class WriteDocument(val uri: Uri) : Intent()
	}

	sealed class State {
		data class Show(
			val companyNumber: String,
			val selectedFilingHistoryItem: FilingHistoryItem,
		) : State()

		data class DocumentDownloaded(
			val companyNumber: String,
			val selectedFilingHistoryItem: FilingHistoryItem,
			val responseBody: ResponseBody
		) : State()

		data class DocumentWritten(
			val companyNumber: String,
			val selectedFilingHistoryItem: FilingHistoryItem,
			val uri: Uri
		) : State()

		class Error(val t: Throwable) : State()

	}

}
