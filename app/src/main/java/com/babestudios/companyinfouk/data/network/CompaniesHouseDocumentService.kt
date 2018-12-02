package com.babestudios.companyinfouk.data.network

import com.babestudios.companyinfouk.BuildConfig

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CompaniesHouseDocumentService {
	@GET(BuildConfig.COMPANIES_HOUSE_GET_DOCUMENT_ENDPOINT)
	fun getDocument(@Header("Authorization") authorization: String,
					@Header("Accept") accept: String,
					@Path("documentNumber") documentNumber: String): Observable<ResponseBody>

}


