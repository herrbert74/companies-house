package com.babestudios.companieshouse.data.network;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.data.model.charges.Charges;
import com.babestudios.companieshouse.data.model.company.Company;
import com.babestudios.companieshouse.data.model.filinghistory.FilingHistoryList;
import com.babestudios.companieshouse.data.model.insolvency.Insolvency;
import com.babestudios.companieshouse.data.model.officers.Officers;
import com.babestudios.companieshouse.data.model.persons.Persons;
import com.babestudios.companieshouse.data.model.search.CompanySearchResult;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface CompaniesHouseService {
	@GET(BuildConfig.COMPANIES_HOUSE_SEARCH_COMPANIES_ENDPOINT)
	Observable<CompanySearchResult> searchCompanies(@Header("Authorization") String authorization,
													@Query("q") String searchTerm,
													@Query("items_per_page") String itemsPerPage,
													@Query("start_index") String startIndex);

	@GET(BuildConfig.COMPANIES_HOUSE_GET_COMPANY_ENDPOINT)
	Observable<Company> getCompany(@Header("Authorization") String authorization,
								   @Path("companyNumber") String companyNumber);


	@GET(BuildConfig.COMPANIES_HOUSE_GET_FILING_HISTORY_ENDPOINT)
	Observable<FilingHistoryList> getFilingHistory(@Header("Authorization") String authorization,
												   @Path("companyNumber") String companyNumber,
												   @Query("category") String category,
												   @Query("items_per_page") String itemsPerPage,
												   @Query("start_index") String startIndex);

	@GET(BuildConfig.COMPANIES_HOUSE_GET_CHARGES_ENDPOINT)
	Observable<Charges> getCharges(@Header("Authorization") String authorization,
								   @Path("companyNumber") String companyNumber,
								   @Query("items_per_page") String itemsPerPage,
								   @Query("start_index") String startIndex);

	@GET(BuildConfig.COMPANIES_HOUSE_GET_INSOLVENCY_ENDPOINT)
	Observable<Insolvency> getInsolvency(@Header("Authorization") String authorization,
										 @Path("companyNumber") String companyNumber);

	@GET(BuildConfig.COMPANIES_HOUSE_GET_OFFICERS_ENDPOINT)
	Observable<Officers> getOfficers(@Header("Authorization") String authorization,
									 @Path("companyNumber") String companyNumber,
									 @Query("registerView") String registerView,
									 @Query("registerType") String registerType,
									 @Query("orderBy") String orderBy,
									 @Query("items_per_page") String itemsPerPage,
									 @Query("start_index") String startIndex);

	@GET(BuildConfig.COMPANIES_HOUSE_GET_PERSONS_ENDPOINT)
	Observable<Persons> getPersons(@Header("Authorization") String authorization,
								   @Path("companyNumber") String companyNumber,
								   @Query("registerView") String registerView,
								   @Query("items_per_page") String itemsPerPage,
								   @Query("start_index") String startIndex);
}


