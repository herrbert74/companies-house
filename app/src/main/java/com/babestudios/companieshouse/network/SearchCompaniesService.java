package com.babestudios.companieshouse.network;

import com.babestudios.companieshouse.BuildConfig;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface SearchCompaniesService {
	@GET(BuildConfig.COMPANIES_HOUSE_SEARCH_COMPANIES_ENDPOINT)
	Observable<String> searchCompanies(@Query("q") String searchTerm,
									   @Query("items_per_page") String itemPerPage,
									   @Query("start_index") String startIndex);
}


