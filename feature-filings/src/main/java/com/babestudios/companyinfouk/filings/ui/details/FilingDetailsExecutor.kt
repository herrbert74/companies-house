package com.babestudios.companyinfouk.filings.ui.details

import android.net.Uri
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsStore.Intent
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsStore.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class FilingDetailsExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
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
			val documentUri = companiesRepository.getDocument(state.documentId)
			withContext(mainContext) { dispatch(Message.DocumentDownloaded(documentUri)) }
		}
	}

	private fun writeDocument(responseBody: ResponseBody, uri: Uri) {
		scope.launch(ioContext) {
			val documentUri = companiesRepository.writeDocumentPdf(responseBody, uri)
			withContext(mainContext) { dispatch(Message.DocumentWritten(documentUri)) }
		}
	}

}
