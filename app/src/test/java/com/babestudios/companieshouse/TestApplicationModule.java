package com.babestudios.companieshouse;

import android.app.Application;
import android.content.Context;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.local.PreferencesHelper;
import com.babestudios.companieshouse.data.network.CompaniesHouseService;
import com.babestudios.companieshouse.data.network.converters.AdvancedGsonConverterFactory;
import com.babestudios.companieshouse.injection.ApplicationContext;
import com.babestudios.companieshouse.injection.ApplicationModule;

import org.mockito.Mockito;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static org.mockito.Mockito.mock;

@Module
public class TestApplicationModule extends ApplicationModule{

	//private final Application application;

	public TestApplicationModule(Application application) {
		super(application);
		//this.application = application;
	}

	@Provides
	@Singleton
	@Named("CompaniesHouseRetrofit")
	Retrofit provideCompaniesHouseRetrofit() {
		return new Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.build();
	}

	@Provides
	@Singleton
	CompaniesHouseService provideCompaniesHouseService(@Named("CompaniesHouseRetrofit") Retrofit retroFit) {
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

	@Provides
	@Singleton
	DataManager provideDataManager(CompaniesHouseService companiesHouseService, PreferencesHelper preferencesHelper) {
		return Mockito.mock(DataManager.class);
	}
}
