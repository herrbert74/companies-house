package com.babestudios.companyinfouk.filings.ui.details

import android.net.Uri
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsStore.Intent
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsStore.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class FilingHistoryDetailsExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, Nothing, State, Message, Nothing>(mainContext) {

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.FetchDocument -> fetchDocument(intent.documentId)
			is Intent.WriteDocument -> writeDocument((getState() as State.DocumentDownloaded).responseBody, intent.uri)
		}
	}

	//region filings details

	private fun fetchDocument(documentId:String) {
		scope.launch {
			val documentUri = companiesRepository.getDocument(documentId)
			dispatch(Message.DocumentDownloaded(documentUri))
		}
	}

	private fun writeDocument(responseBody: ResponseBody, uri: Uri) {
		scope.launch {
			val documentUri = companiesRepository.writeDocumentPdf(responseBody, uri)
			dispatch(Message.DocumentWritten(documentUri))
		}
	}

}
