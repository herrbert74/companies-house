package com.babestudios.companieshouse.injection;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.local.PreferencesHelper;
import com.babestudios.companieshouse.data.network.CompaniesHouseDocumentService;
import com.babestudios.companieshouse.data.network.CompaniesHouseService;
import com.babestudios.companieshouse.data.network.converters.AdvancedGsonConverterFactory;
import com.babestudios.companieshouse.utils.Base64Wrapper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

@Module
public class ApplicationModule {

	protected final Application application;

	public ApplicationModule(CompaniesHouseApplication application) {
		this.application = application;
	}


	@Provides
	@Singleton
	@Named("CompaniesHouseRetrofit")
	Retrofit provideCompaniesHouseRetrofit() {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
		httpClient.addInterceptor(logging);
		return new Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.client(httpClient.build())//
				.build();
	}

	@Provides
	@Singleton
	@Named("CompaniesHouseDocumentRetrofit")
	Retrofit provideCompaniesHouseDocumentRetrofit() {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
		httpClient.addInterceptor(logging);
		return new Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_DOCUMENT_API_BASE_URL)//
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.client(httpClient.build())//
				.build();
	}

	@Provides
	@Singleton
	CompaniesHouseService provideCompaniesHouseService(@Named("CompaniesHouseRetrofit") Retrofit retroFit) {
		return retroFit.create(CompaniesHouseService.class);
	}

	@Provides
	@Singleton
	CompaniesHouseDocumentService provideCompaniesHouseDocumentService(@Named("CompaniesHouseDocumentRetrofit") Retrofit retroFit) {
		return retroFit.create(CompaniesHouseDocumentService.class);
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
	DataManager provideDataManager(CompaniesHouseService companiesHouseService, CompaniesHouseDocumentService companiesHouseDocumentService, PreferencesHelper preferencesHelper, Base64Wrapper base64Wrapper) {
		Log.d("test", "provideDataManager: ");
		return new DataManager(companiesHouseService, companiesHouseDocumentService, preferencesHelper, base64Wrapper);
	}

	@Provides
	Base64Wrapper provideBase64Wrapper() {
		return new Base64Wrapper();
	}
}
