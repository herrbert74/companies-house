package com.babestudios.companyinfouk.shared.data.network

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.statement.HttpResponse

interface CompaniesHouseDocumentApi {

	@GET("document/{documentNumber}/content")
	suspend fun getDocument(
		@Header("Accept") accept: String,
		@Path("documentNumber") documentNumber: String,
	): HttpResponse

}
