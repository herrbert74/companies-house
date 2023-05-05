package com.babestudios.companyinfouk.data.network

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Path
import okhttp3.ResponseBody

interface CompaniesHouseDocumentApi {
	@GET("document/{documentNumber}/content")
	suspend fun getDocument(
		@Header("Accept") accept: String,
		@Path("documentNumber") documentNumber: String,
	): ResponseBody

}


