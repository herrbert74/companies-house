package com.babestudios.companyinfouk.data.di

import android.content.Context
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesAccessor
import com.babestudios.companyinfouk.data.local.PREF_FILE_NAME
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.local.apilookup.ChargesHelper
import com.babestudios.companyinfouk.data.local.apilookup.ChargesHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.PscHelper
import com.babestudios.companyinfouk.data.local.apilookup.PscHelperContract
import com.babestudios.companyinfouk.data.mappers.CompaniesHouseMapper
import com.babestudios.companyinfouk.data.mappers.CompaniesHouseMapping
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.data.network.interceptors.CompaniesHouseInterceptor
import com.babestudios.companyinfouk.data.utils.Base64Wrapper
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.data.utils.RawResourceHelperContract
import com.babestudios.companyinfouk.data.utils.StringResourceHelper
import com.babestudios.companyinfouk.data.utils.StringResourceHelperContract
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val dataModule = module {

	single(named("CompaniesHouseRetrofit")) {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()
		httpClient.addInterceptor(logging)
		httpClient.addInterceptor(
			ChuckerInterceptor.Builder(androidContext())
				.collector(ChuckerCollector(androidContext(), showNotification = false))
				.build()
		)
		httpClient.addInterceptor(CompaniesHouseInterceptor(Base64Wrapper()))
		Retrofit.Builder()
			.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)
			.addConverterFactory(AdvancedGsonConverterFactory.create())
			.client(httpClient.build())
			.build()
	}

	singleOf(::CompaniesHouseMapper) { bind<CompaniesHouseMapping>() }

	single {
		val retrofit: Retrofit = get(named("CompaniesHouseRetrofit"))
		retrofit.create(CompaniesHouseService::class.java)
	}

	single {
		CompaniesAccessor(
			androidContext(),
			get(),
			get(),
			get(),
			get(),
			get(),
			get(named("IoDispatcher"))
		)
	}.withOptions {
		bind<CompaniesRepository>()
	}

	single {
		RawResourceHelper(androidContext())
	}.withOptions {
		bind<RawResourceHelperContract>()
	}

	singleOf(::PscHelper) { bind<PscHelperContract>() }
	singleOf(::ChargesHelper) { bind<ChargesHelperContract>() }
	singleOf(::FilingHistoryDescriptionsHelper) { bind<FilingHistoryDescriptionsHelperContract>() }
	singleOf(::ConstantsHelper) { bind<ConstantsHelperContract>() }
	singleOf(::StringResourceHelper) { bind<StringResourceHelperContract>() }

	single {
		StringResourceHelper(androidContext())
	}.withOptions {
		bind<StringResourceHelperContract>()
	}
	single { androidContext().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE) }
	single { FirebaseAnalytics.getInstance(androidContext()) }

	single {
		val retrofit: Retrofit = get(named("CompaniesHouseDocumentRetrofit"))
		retrofit.create(CompaniesHouseDocumentService::class.java)
	}

	single {
		GsonBuilder()
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
			.create()
	}

	single { PreferencesHelper(get(), get()) }

	single(named("CompaniesHouseDocumentRetrofit")) {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()
		httpClient.addInterceptor(logging)
		httpClient.addInterceptor(CompaniesHouseInterceptor(Base64Wrapper()))
		Retrofit.Builder()
			.baseUrl(BuildConfig.COMPANIES_HOUSE_DOCUMENT_API_BASE_URL)
			.addConverterFactory(AdvancedGsonConverterFactory.create())
			.client(httpClient.build())
			.build()
	}

}
