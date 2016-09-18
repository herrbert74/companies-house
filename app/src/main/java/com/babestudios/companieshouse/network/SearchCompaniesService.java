package com.babestudios.companieshouse.network;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.search.pojos.CompanySearchResult;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

public interface SearchCompaniesService {
	@GET(BuildConfig.COMPANIES_HOUSE_SEARCH_COMPANIES_ENDPOINT)
	Observable<CompanySearchResult> searchCompanies(@Header("Authorization") String authorization,
													@Query("q") String searchTerm,
													@Query("items_per_page") String itemsPerPage,
													@Query("start_index") String startIndex);
}


