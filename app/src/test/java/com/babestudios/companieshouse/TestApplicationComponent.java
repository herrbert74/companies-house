package com.babestudios.companieshouse;

import com.babestudios.companieshouse.data.network.CompaniesHouseService;
import com.babestudios.companieshouse.injection.ApplicationComponent;

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

	CompaniesHouseService getSearchCompaniesService();
}
