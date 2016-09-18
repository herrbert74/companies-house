package com.babestudios.companieshouse;

import com.babestudios.companieshouse.network.SearchCompaniesService;
import com.babestudios.companieshouse.search.SearchPresenter;
import com.babestudios.companieshouse.search.SearchResultsAdapter;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
	void inject(SearchPresenter searchPresenter);
	void inject(SearchResultsAdapter searchResultsAdapter);

	@Named("SearchCompaniesRetrofit")
	Retrofit getSearchCompaniesRetrofit();

	SearchCompaniesService getSearchCompaniesService();

}
