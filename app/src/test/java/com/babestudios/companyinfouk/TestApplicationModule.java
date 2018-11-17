package com.babestudios.companyinfouk;

import android.app.Application;
import android.content.Context;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.local.PreferencesHelper;
import com.babestudios.companyinfouk.data.network.CompaniesHouseService;
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory;
import com.babestudios.companyinfouk.injection.ApplicationContext;

import org.mockito.Mockito;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Module
public class TestApplicationModule{

	private Application application;

	public TestApplicationModule(Application application) {
		this.application = application;
	}

	@Provides
	@Singleton
	@Named("CompaniesHouseRetrofit")
	Retrofit provideCompaniesHouseRetrofit() {
		return new Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
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
