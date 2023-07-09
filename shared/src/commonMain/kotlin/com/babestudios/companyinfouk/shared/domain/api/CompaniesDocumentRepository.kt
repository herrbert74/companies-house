package com.babestudios.companyinfouk.shared.domain.api

import com.eygraber.uri.Uri
import io.ktor.client.statement.HttpResponse

interface CompaniesDocumentRepository {
	suspend fun getDocument(documentId: String): HttpResponse
	suspend fun writeDocumentPdf(responseBody: HttpResponse, uri: Uri): Uri
}
