package com.babestudios.companieshouse;

import com.babestudios.companieshouse.data.network.CompaniesHouseService;
import com.babestudios.companieshouse.data.network.converters.AdvancedGsonConverterFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

@Module
public class TestApplicationModule {

	/*@Provides
	@Singleton
	@Named("SearchCompaniesRetrofit")
	MockRetrofit provideSearchCompaniesRetrofit() {
		return new MockRetrofit.Builder(new Retrofit.Builder().build())//
				.build();
	}

	@Provides
	@Singleton
	CompaniesHouseService provideSearchCompaniesService(@Named("SearchCompaniesRetrofit") Retrofit retroFit) {
		return retroFit.create(CompaniesHouseService.class);
	}

	@Provides
	@Singleton
	@Named("SearchCompaniesRetrofit")
	Retrofit provideSearchCompaniesRetrofit() {
		return new Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.build();
	}

	@Provides
	@Singleton
	CompaniesHouseService provideSearchCompaniesService(@Named("SearchCompaniesRetrofit") Retrofit retroFit) {
		return retroFit.create(CompaniesHouseService.class);
	}*/
}
