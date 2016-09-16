package com.babestudios.companieshouse;

import android.content.Context;

import com.babestudios.companieshouse.network.SearchCompaniesService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
	Context context();
	void inject(CompaniesHouseApplication application);

	@Named("SearchCompaniesRetrofit")
	Retrofit getSearchCompaniesRetrofit();

	SearchCompaniesService getSearchCompaniesService();

}
