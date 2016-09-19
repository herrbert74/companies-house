package com.babestudios.companieshouse;

import com.babestudios.companieshouse.network.SearchCompaniesService;
import com.babestudios.companieshouse.network.converters.AdvancedGsonConverterFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.mock.MockRetrofit;

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
	SearchCompaniesService provideSearchCompaniesService(@Named("SearchCompaniesRetrofit") Retrofit retroFit) {
		return retroFit.create(SearchCompaniesService.class);
	}*/

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
	SearchCompaniesService provideEarningsDatesService(@Named("SearchCompaniesRetrofit") Retrofit retroFit) {
		return retroFit.create(SearchCompaniesService.class);
	}
}
