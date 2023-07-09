package com.babestudios.companyinfouk.filings.ui.details

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsStore.Intent
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsStore.State
import com.babestudios.companyinfouk.shared.domain.api.CompaniesDocumentRepository
import com.eygraber.uri.Uri
import com.eygraber.uri.toUri
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilingDetailsExecutor constructor(
	private val companiesRepository: CompaniesDocumentRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, Nothing, State, Message, Nothing>(mainContext) {

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.FetchDocument -> fetchDocument(getState())
			is Intent.WriteDocument -> writeDocument(getState().downloadedPdfResponseBody!!, intent.uri)
		}
	}

	//region filings details

	private fun fetchDocument(state: State) {
		scope.launch(ioContext) {
			val documentResponse = companiesRepository.getDocument(state.documentId)
			withContext(mainContext) { dispatch(Message.DocumentDownloaded(documentResponse)) }
		}
	}

	private fun writeDocument(responseBody: HttpResponse, uri: android.net.Uri) {
		scope.launch(ioContext) {
			val documentUri = companiesRepository.writeDocumentPdf(responseBody, uri.toUri())
			withContext(mainContext) { dispatch(Message.DocumentWritten(documentUri)) }
		}
	}

}
