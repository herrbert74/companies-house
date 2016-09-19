package com.babestudios.companieshouse;

import com.babestudios.companieshouse.network.SearchCompaniesService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {TestApplicationModule.class})
public interface TestApplicationComponent extends ApplicationComponent {
	void inject(CompaniesHouseApplication application);

	@Named("SearchCompaniesRetrofit")
	Retrofit getSearchCompaniesRetrofit();

	SearchCompaniesService getSearchCompaniesService();
}
