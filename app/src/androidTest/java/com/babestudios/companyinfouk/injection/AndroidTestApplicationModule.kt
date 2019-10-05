package com.babestudios.companyinfouk.injection

//import com.babestudios.companyinfouk.CompaniesRepository
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.BuildConfig
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.TestHelper
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.data.utils.Base64Wrapper
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.data.utils.RawResourceHelperContract
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
class AndroidTestApplicationModule(private val application: Application) {

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
		return Mockito.mock(CompaniesHouseApplication::class.java)
	}

	@Provides
	@ApplicationContext
	internal fun provideContext(): Context {
		return Mockito.mock(CompaniesHouseApplication::class.java)
	}

	@Provides
	internal fun provideGson(): Gson {
		return GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
				.create()
	}

	@Provides
	internal fun provideSharedPreferences() : SharedPreferences {
		return Mockito.mock(SharedPreferences::class.java)
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
		return Mockito.mock(CompaniesRepository::class.java)
	}

	@Provides
	@Singleton
	internal fun provideSchedulerProvider(): SchedulerProvider {
		return SchedulerProvider(Schedulers.trampoline(), Schedulers.trampoline())
	}

	@Provides
	@Singleton
	internal fun provideRawResourceHelperContract(): RawResourceHelperContract {
		return RawResourceHelper(application)
	}

	@Provides
	@Singleton
	internal fun provideTestHelper(): TestHelper {
		return TestHelper()
	}
}
