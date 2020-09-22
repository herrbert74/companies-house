package com.babestudios.companyinfouk.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.data.utils.Base64Wrapper
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.data.utils.RawResourceHelperContract
import com.babestudios.companyinfouk.data.utils.errors.CompaniesHouseErrorResolver
import com.babestudios.companyinfouk.data.utils.errors.apilookup.ErrorHelper
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AndroidTestDataModule(private val context: Context) {

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
		return Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_DOCUMENT_API_BASE_URL)//
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.client(httpClient.build())//
				.build()
	}

	@Provides
	@Singleton
	internal fun provideCompaniesHouseService(@Named("CompaniesHouseRetrofit") retroFit: Retrofit): CompaniesHouseService {
		return retroFit.create(CompaniesHouseService::class.java)
	}

	@Provides
	@Singleton
	internal fun provideCompaniesHouseDocumentService(@Named("CompaniesHouseRetrofit") retroFit: Retrofit): CompaniesHouseDocumentService {
		return retroFit.create(CompaniesHouseDocumentService::class.java)
	}

	@Provides
	internal fun provideApplication(): Application {
		return mockk<CompaniesHouseApplication>()
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
	internal fun provideCompaniesRepositoryContract(
			companiesHouseService: CompaniesHouseService,
			companiesHouseDocumentService: CompaniesHouseDocumentService,
			preferencesHelper: PreferencesHelper,
			base64Wrapper: Base64Wrapper,
			constantsHelper: ConstantsHelper,
			filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelper
	): CompaniesRepositoryContract {
		val mockCompaniesRepository = mockk<CompaniesRepositoryContract>()

		val favourites = listOf(SearchHistoryItem(
				"Acme Painting",
				"1",
				111L
		))

		every {
			mockCompaniesRepository.favourites()
		} returns Single.create { it.onSuccess(favourites) }

		every {
			mockCompaniesRepository.logAppOpen()
		} returns Unit

		every {
			mockCompaniesRepository.logScreenView(any())
		} returns Unit

		every {
			mockCompaniesRepository.logSearch(any())
		} returns Unit

		every {
			mockCompaniesRepository.recentSearches()
		} returns Single.create { it.onSuccess(favourites) }

		return mockCompaniesRepository
	}

	@Provides
	@Singleton
	internal fun provideSchedulerProvider(): SchedulerProvider {
		return SchedulerProvider(Schedulers.trampoline(), Schedulers.trampoline())
	}

	@Provides
	@Singleton
	internal fun provideRawResourceHelperContract(): RawResourceHelperContract {
		return RawResourceHelper(context)
	}

	@Provides
	@Singleton
	internal fun provideErrorHelper(): ErrorHelper {
		return ErrorHelper(context)
	}

	@Provides
	@Singleton
	internal fun provideErrorResolver(errorHelper: ErrorHelper): ErrorResolver {
		return CompaniesHouseErrorResolver(errorHelper)
	}

	@Provides
	@Singleton
	internal fun provideFirebaseAnalytics(): FirebaseAnalytics {
		return FirebaseAnalytics.getInstance(context)
	}

}
