package com.babestudios.companieshouse;

import android.content.Context;

import com.babestudios.companieshouse.network.SearchCompaniesService;
import com.babestudios.companieshouse.network.converters.AdvancedGsonConverterFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

@Module
class ApplicationModule {

	private final Context context;

	ApplicationModule(Context context) {
		this.context = context;
	}

	@Provides
	public Context context() {
		return context;
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
	SearchCompaniesService provideSearchCompaniesService(@Named("SearchCompaniesRetrofit") Retrofit retroFit) {
		return retroFit.create(SearchCompaniesService.class);
	}
}
