package com.babestudios.companieshouse.injection;

import com.babestudios.companieshouse.data.network.CompaniesHouseService;
import com.babestudios.companieshouse.ui.search.SearchPresenter;
import com.babestudios.companieshouse.ui.search.SearchResultsAdapter;

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

	CompaniesHouseService getSearchCompaniesService();

}
