package com.babestudios.companyinfouk.data.di

import android.content.Context
import android.content.SharedPreferences
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.local.PREF_FILE_NAME
import com.babestudios.companyinfouk.data.local.apilookup.ChargesHelper
import com.babestudios.companyinfouk.data.local.apilookup.ChargesHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.PscHelper
import com.babestudios.companyinfouk.data.local.apilookup.PscHelperContract
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.data.network.interceptors.CompaniesHouseInterceptor
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.data.utils.StringResourceHelper
import com.babestudios.companyinfouk.data.utils.StringResourceHelperContract
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
object DataModule {

	@Provides
	@Singleton
	@Named("CompaniesHouseRetrofit")
	internal fun provideCompaniesHouseRetrofit(
		@ApplicationContext context: Context,
		companiesHouseInterceptor: CompaniesHouseInterceptor
	): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()
		httpClient.addInterceptor(logging)
		httpClient.addInterceptor(
			ChuckerInterceptor.Builder(context)
				.collector(ChuckerCollector(context, showNotification = false))
				.build()
		)
		httpClient.addInterceptor(companiesHouseInterceptor)
		return Retrofit.Builder()
			.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addConverterFactory(AdvancedGsonConverterFactory.create())
			.client(httpClient.build())
			.build()
	}

	@Provides
	@Singleton
	@Named("CompaniesHouseDocumentRetrofit")
	internal fun provideCompaniesHouseDocumentRetrofit(companiesHouseInterceptor: CompaniesHouseInterceptor): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()
		httpClient.addInterceptor(logging)
		httpClient.addInterceptor(companiesHouseInterceptor)
		return Retrofit.Builder()
			.baseUrl(BuildConfig.COMPANIES_HOUSE_DOCUMENT_API_BASE_URL)
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addConverterFactory(AdvancedGsonConverterFactory.create())
			.client(httpClient.build())
			.build()
	}

	@Provides
	@Singleton
	internal fun provideCompaniesHouseService(@Named("CompaniesHouseRetrofit") retroFit: Retrofit)
		: CompaniesHouseService {
		return retroFit.create(CompaniesHouseService::class.java)
	}

	@Provides
	@Singleton
	internal fun provideCompaniesHouseDocumentService(@Named("CompaniesHouseDocumentRetrofit") retroFit: Retrofit)
		: CompaniesHouseDocumentService {
		return retroFit.create(CompaniesHouseDocumentService::class.java)
	}

	@Provides
	internal fun provideGson(): Gson {
		return GsonBuilder()
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
			.create()
	}

	@Provides
	internal fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
		return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
	}

	@Provides
	@Singleton
	internal fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
		return FirebaseAnalytics.getInstance(context)
	}

	@Provides
	@Singleton
	fun provideStringResourceHelper(@ApplicationContext context: Context): StringResourceHelperContract =
		StringResourceHelper(context)

	@Provides
	@Singleton
	fun provideConstantsHelper(rawResourceHelper: RawResourceHelper): ConstantsHelperContract =
		ConstantsHelper(rawResourceHelper)

	@Provides
	@Singleton
	fun provideFilingHistoryDescriptionsHelper(rawResourceHelper: RawResourceHelper)
		: FilingHistoryDescriptionsHelperContract = FilingHistoryDescriptionsHelper(rawResourceHelper)

	@Provides
	@Singleton
	fun provideChargesHelper(rawResourceHelper: RawResourceHelper)
		: ChargesHelperContract = ChargesHelper(rawResourceHelper)

	@Provides
	@Singleton
	fun providePscHelper(rawResourceHelper: RawResourceHelper)
		: PscHelperContract = PscHelper(rawResourceHelper)

}
