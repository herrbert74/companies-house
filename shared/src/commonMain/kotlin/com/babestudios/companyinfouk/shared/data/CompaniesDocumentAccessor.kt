package com.babestudios.companyinfouk.shared.data

import com.babestudios.companyinfouk.shared.data.network.CompaniesHouseDocumentApi
import com.babestudios.companyinfouk.shared.domain.api.CompaniesDocumentRepository
import com.diamondedge.logging.logging
import com.eygraber.uri.Uri
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.files.FileNotFoundException

class CompaniesDocumentAccessor(
	private val companiesHouseDocumentApi: CompaniesHouseDocumentApi,
	private val ioContext: CoroutineDispatcher,
) : CompaniesDocumentRepository {

	private val log = logging()

	override suspend fun getDocument(documentId: String): HttpResponse {
		return withContext(ioContext) {
			companiesHouseDocumentApi.getDocument("application/pdf", documentId)
		}
	}

	override suspend fun writeDocumentPdf(responseBody: HttpResponse, uri: Uri): Uri {
		return withContext(Dispatchers.IO) {
			val byteReadChannel = responseBody.bodyAsChannel()
			try {
				byteReadChannel.writeToFile(uri)
			} catch (fileNotFoundException: FileNotFoundException) {
				log.e(fileNotFoundException) { "zsoltbertalan* File not found: ${fileNotFoundException.message}" }
			} catch (ioException: IOException) {
				log.e(ioException) { "zsoltbertalan* Error during closing input stream ${ioException.message}" }
			}
			uri
		}
	}

}
