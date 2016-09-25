package com.babestudios.companieshouse.injection;

import android.content.Context;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.data.network.CompaniesHouseService;
import com.babestudios.companieshouse.data.network.converters.AdvancedGsonConverterFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

@Module
public class ApplicationModule {

	private final Context context;

	public ApplicationModule(Context context) {
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
	CompaniesHouseService provideSearchCompaniesService(@Named("SearchCompaniesRetrofit") Retrofit retroFit) {
		return retroFit.create(CompaniesHouseService.class);
	}
}
