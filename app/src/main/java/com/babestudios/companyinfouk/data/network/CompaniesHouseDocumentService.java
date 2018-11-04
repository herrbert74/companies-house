package com.babestudios.companyinfouk.data.network;

import com.babestudios.companyinfouk.BuildConfig;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface CompaniesHouseDocumentService {
	@GET(BuildConfig.COMPANIES_HOUSE_GET_DOCUMENT_ENDPOINT)
	Observable<ResponseBody> getDocument(@Header("Authorization") String authorization,
										 @Header("Accept") String accept,
										 @Path("documentNumber") String documentNumber);

}


