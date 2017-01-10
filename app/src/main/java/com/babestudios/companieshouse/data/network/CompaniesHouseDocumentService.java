package com.babestudios.companieshouse.data.network;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.data.model.charges.Charges;
import com.babestudios.companieshouse.data.model.company.Company;
import com.babestudios.companieshouse.data.model.filinghistory.FilingHistoryList;
import com.babestudios.companieshouse.data.model.insolvency.Insolvency;
import com.babestudios.companieshouse.data.model.officers.Officers;
import com.babestudios.companieshouse.data.model.officers.appointments.Appointments;
import com.babestudios.companieshouse.data.model.persons.Persons;
import com.babestudios.companieshouse.data.model.search.CompanySearchResult;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface CompaniesHouseDocumentService {
	@GET(BuildConfig.COMPANIES_HOUSE_GET_DOCUMENT_ENDPOINT)
	Observable<ResponseBody> getDocument(@Header("Authorization") String authorization,
										 @Header("Accept") String accept,
										 @Path("documentNumber") String documentNumber);

}


