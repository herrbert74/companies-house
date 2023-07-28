package com.babestudios.companyinfouk.shared.data

import com.babestudios.companyinfouk.shared.data.network.CompaniesHouseDocumentApi
import com.babestudios.companyinfouk.shared.domain.api.CompaniesDocumentRepository
import com.eygraber.uri.Uri
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CompaniesDocumentAccessor(
	private val companiesHouseDocumentApi: CompaniesHouseDocumentApi,
	private val ioContext: CoroutineDispatcher,
) : CompaniesDocumentRepository {

	//private val log = logging()

	override suspend fun getDocument(documentId: String): HttpResponse {
		return withContext(ioContext) {
			companiesHouseDocumentApi.getDocument("application/pdf", documentId)
		}
	}

	override suspend fun writeDocumentPdf(responseBody: HttpResponse, uri: Uri): Uri {
		return uri

		/*return withContext(Dispatchers.IO) {
			val outputStream = context.contentResolver.openOutputStream(uri)
			val inputStream = responseBody.bodyAsChannel()//responseBody.byteStream()
			try {
				@Suppress("MagicNumber")
				val fileReader = ByteArray(4096)
				while (true) {
					val read = inputStream.read(1, fileReader)
					if (read == -1) {
						break
					}
					outputStream?.write(fileReader, 0, read)
				}
				outputStream?.flush()
			} catch (e: FileNotFoundException) {
				log.e {"File not found: ${e.localizedMessage}"}
			} catch (e: IOException) {
				log.d{"Error during closing input stream ${e.localizedMessage}"}
			} finally {
				inputStream.close()

				outputStream?.close()
			}
			uri
		}*/
	}

}
