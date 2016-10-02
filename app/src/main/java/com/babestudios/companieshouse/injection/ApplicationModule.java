package com.babestudios.companieshouse.injection;

import android.app.Application;
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

	protected final Application application;

	public ApplicationModule(Application application) {
		this.application = application;
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

	@Provides
	Application provideApplication() {
		return application;
	}

	@Provides
	@ApplicationContext
	Context provideContext() {
		return application;
	}
}
