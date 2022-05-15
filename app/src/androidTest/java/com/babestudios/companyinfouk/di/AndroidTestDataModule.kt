package com.babestudios.companyinfouk.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.di.DataModule
import com.babestudios.companyinfouk.data.network.CompaniesHouseRxDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseRxService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.data.utils.RawResourceHelperContract
import com.babestudios.companyinfouk.data.utils.errors.CompaniesHouseRxErrorResolver
import com.babestudios.companyinfouk.data.utils.errors.apilookup.ErrorHelper
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
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
	): CompaniesHouseRxService {
		return retroFit.create(CompaniesHouseRxService::class.java)
	}

	@Provides
	@Singleton
	internal fun provideCompaniesHouseDocumentService(
		@Named("CompaniesHouseRetrofit") retroFit: Retrofit
	): CompaniesHouseRxDocumentService {
		return retroFit.create(CompaniesHouseRxDocumentService::class.java)
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
	internal fun provideCompaniesRepositoryContract(): CompaniesRxRepository {
		val mockCompaniesRepository = mockk<CompaniesRxRepository>()

		val favourites = listOf(
			SearchHistoryItem(
				"Acme Painting",
				"1",
				111L
			)
		)

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
		return CompaniesHouseRxErrorResolver(errorHelper)
	}

	@Provides
	@Singleton
	internal fun provideFirebaseAnalytics(): FirebaseAnalytics {
		return FirebaseAnalytics.getInstance(context)
	}

}
