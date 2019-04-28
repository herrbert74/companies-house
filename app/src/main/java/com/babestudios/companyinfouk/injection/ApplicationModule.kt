package com.babestudios.companyinfouk.injection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.BuildConfig
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.local.PREF_FILE_NAME
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.utils.Base64Wrapper
import com.babestudios.companyinfouk.utils.RawResourceHelper
import com.babestudios.companyinfouk.utils.RawResourceHelperContract
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule(application: CompaniesHouseApplication) {

	protected val application: Application

	init {
		this.application = application
	}


	@Provides
	@Singleton
	@Named("CompaniesHouseRetrofit")
	internal fun provideCompaniesHouseRetrofit(): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()
		httpClient.addInterceptor(logging)
		httpClient.addInterceptor(ChuckInterceptor(CompaniesHouseApplication.context))
		return Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.client(httpClient.build())//
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
	internal fun provideGson(): Gson {
		return GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
				.create()
	}

	@Provides
	internal fun provideSharedPreferences() :SharedPreferences {
		return application.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
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
		return CompaniesRepository(
				companiesHouseService,
				companiesHouseDocumentService,
				preferencesHelper,
				base64Wrapper,
				constantsHelper,
				filingHistoryDescriptionsHelper
		)
	}

	@Provides
	@Singleton
	internal fun provideSchedulerProvider(): SchedulerProvider {
		return SchedulerProvider(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR), AndroidSchedulers.mainThread())
	}

	@Provides
	@Singleton
	internal fun provideRawResourceHelperContract(): RawResourceHelperContract {
		return RawResourceHelper(application)
	}

}
