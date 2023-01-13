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
import com.babestudios.companyinfouk.data.local.apilookup.PscHelperContract
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.data.utils.StringResourceHelper
import com.babestudios.companyinfouk.data.utils.StringResourceHelperContract
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@Suppress("unused")
@TestInstallIn(components = [SingletonComponent::class], replaces = [DataModule::class])
class TestDataModule {

	@Provides
	@Singleton
	@Named("CompaniesHouseRetrofit")
	internal fun provideCompaniesHouseRetrofit(): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()
		httpClient.addInterceptor(logging)
		return Retrofit.Builder()//
			.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
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
			.addConverterFactory(AdvancedGsonConverterFactory.create())//
			.client(httpClient.build())//
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
	fun provideStringResourceHelper(): StringResourceHelperContract {
		val stringResourceHelper = mockk<StringResourceHelper>()
		val slotDate = slot<String>()
		val slotFrom = slot<String>()
		val slotFrom2 = slot<String>()
		val slotTo = slot<String>()
		every { stringResourceHelper.getCompanyAccountsNotFoundString() } returns ""
		every { stringResourceHelper.getLastAccountMadeUpToString(any(), capture(slotDate)) } answers {
			"Last account made up to ${slotDate.captured}"
		}
		every { stringResourceHelper.getAppointedFromString(capture(slotFrom)) } answers {
			String.format("From %1\$s", slotFrom.captured)
		}
		every { stringResourceHelper.getAppointedFromToString(capture(slotFrom2), capture(slotTo)) } answers {
			String.format("From %1\$s to %2\$s", slotFrom2.captured, slotTo.captured)
		}
		return stringResourceHelper
	}

	@Provides
	@Singleton
	fun provideConstantsHelper(): ConstantsHelperContract {
		val constantsHelper = mockk<ConstantsHelper>()
		every { constantsHelper.sicLookUp("68100") } returns "Buying and selling of own real estate"
		every { constantsHelper.accountTypeLookUp(any()) } returns ""
		every { constantsHelper.insolvencyCaseType(any()) } returns ""
		every { constantsHelper.insolvencyCaseDateType(any()) } returns ""
		every { constantsHelper.officerRoleLookup(any()) } returns ""
		return constantsHelper
	}

	@Provides
	@Singleton
	fun provideFilingHistoryDescriptionsHelper(): FilingHistoryDescriptionsHelperContract {
		val filingHistoryDescriptionsHelper = mockk<FilingHistoryDescriptionsHelper>()
		every { filingHistoryDescriptionsHelper.filingHistoryLookUp(any()) } returns ""
		return filingHistoryDescriptionsHelper
	}

	@Provides
	@Singleton
	fun provideChargesHelper(): ChargesHelperContract {
		val chargesHelper = mockk<ChargesHelper>()
		every { chargesHelper.statusLookUp(any()) } returns ""
		every { chargesHelper.filingTypeLookUp(any()) } returns ""
		return chargesHelper
	}

	@Provides
	@Singleton
	fun providePscHelper(): PscHelperContract {
		val pscHelper = mockk<PscHelperContract>()
		every { pscHelper.shortDescriptionLookUp(any()) } returns ""
		every { pscHelper.kindLookUp(any()) } returns ""
		return pscHelper
	}

	//endregion

}
