package com.babestudios.companyinfouk.di

import android.content.Context
import android.content.SharedPreferences
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.di.DataModule
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.data.utils.errors.apilookup.ErrorHelper
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

@Suppress("unused")
@Module
@TestInstallIn(
	components = [SingletonComponent::class],
	replaces = [DataModule::class]
)
object AndroidTestDataModule {

	@Provides
	@Singleton
	@Named("CompaniesHouseRetrofit")
	internal fun provideCompaniesHouseRetrofit(): Retrofit {
		return Retrofit.Builder()
			.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addConverterFactory(AdvancedGsonConverterFactory.create())
			.build()
	}

	@Provides
	@Singleton
	@Named("CompaniesHouseDocumentRetrofit")
	internal fun provideCompaniesHouseDocumentRetrofit(): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()
		httpClient.addInterceptor(logging)
		return Retrofit.Builder()
			.baseUrl(BuildConfig.COMPANIES_HOUSE_DOCUMENT_API_BASE_URL)
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addConverterFactory(AdvancedGsonConverterFactory.create())
			.client(httpClient.build())
			.build()
	}

	@Provides
	@Singleton
	internal fun provideCompaniesHouseService(
		@Named("CompaniesHouseRetrofit") retroFit: Retrofit
	): CompaniesHouseService {
		return retroFit.create(CompaniesHouseService::class.java)
	}

	@Provides
	@Singleton
	internal fun provideCompaniesHouseDocumentService(
		@Named("CompaniesHouseRetrofit") retroFit: Retrofit
	): CompaniesHouseDocumentService {
		return retroFit.create(CompaniesHouseDocumentService::class.java)
	}

	@Provides
	@ApplicationContext
	internal fun provideContext(): Context {
		return mockk<CompaniesHouseApplication>()
	}

	@Provides
	internal fun provideGson(): Gson {
		return GsonBuilder()
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
			.create()
	}

	@Provides
	internal fun provideSharedPreferences(): SharedPreferences {
		return mockk()
	}

	@Provides
	@Singleton
	internal fun provideSchedulerProvider(): SchedulerProvider {
		return SchedulerProvider(Schedulers.trampoline(), Schedulers.trampoline())
	}

	@Provides
	@Singleton
	internal fun provideErrorHelper(@ApplicationContext context: Context): ErrorHelper {
		return ErrorHelper(context)
	}


	@Provides
	@Singleton
	internal fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
		return FirebaseAnalytics.getInstance(context)
	}

}
