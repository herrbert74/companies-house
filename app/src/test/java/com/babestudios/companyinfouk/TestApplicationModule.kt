package com.babestudios.companyinfouk

import android.app.Application
import android.content.Context
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.injection.ApplicationContext
import com.babestudios.companyinfouk.utils.Base64Wrapper
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.mockito.Mockito
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class TestApplicationModule(private val application: Application) {

	@Provides
	@Singleton
	@Named("CompaniesHouseRetrofit")
	internal fun provideCompaniesHouseRetrofit(): Retrofit {
		return Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
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
	internal fun provideCompaniesHouseDocumentService(@Named("CompaniesHouseDocumentRetrofit") retroFit: Retrofit): CompaniesHouseDocumentService {
		return retroFit.create(CompaniesHouseDocumentService::class.java)
	}

	@Provides
	internal fun provideApplication(): Application {
		return application
	}

	@Provides
	@ApplicationContext
	internal fun provideContext(): Context {
		return application
	}

	@Provides
	@Singleton
	internal fun provideDataManager(companiesHouseService: CompaniesHouseService, preferencesHelper: PreferencesHelper): CompaniesRepository {
		return Mockito.mock(CompaniesRepository::class.java)
	}

	@Provides
	@Singleton
	internal fun provideCompaniesRepository(
			companiesHouseService: CompaniesHouseService,
			companiesHouseDocumentService: CompaniesHouseDocumentService,
			preferencesHelper: PreferencesHelper,
			base64Wrapper: Base64Wrapper,
			constantsHelper: ConstantsHelper,
			filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelper
	): CompaniesRepositoryContract {
		return Mockito.mock(CompaniesRepository::class.java)
	}

	@Provides
	internal fun provideBase64Wrapper(): Base64Wrapper {
		return Base64Wrapper()
	}

	@Provides
	internal fun provideConstantsHelper(): ConstantsHelper {
		return ConstantsHelper()
	}

	@Provides
	internal fun provideFilingHistoryDescriptionsHelper(): FilingHistoryDescriptionsHelper {
		return FilingHistoryDescriptionsHelper()
	}

	@Provides
	internal fun provideSchedulerProvider(): SchedulerProvider {
		return SchedulerProvider(Schedulers.trampoline(), Schedulers.trampoline())
	}

}
