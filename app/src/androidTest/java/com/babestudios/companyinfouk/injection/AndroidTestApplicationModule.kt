package com.babestudios.companyinfouk.injection

import android.app.Application
import android.content.Context
import com.babestudios.companyinfouk.BuildConfig
import com.babestudios.companyinfouk.data.AndroidTestCompaniesRepository
//import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.ui.favourites.FavouritesPresenter
import com.babestudios.companyinfouk.utils.Base64Wrapper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dagger.Module
import dagger.Provides
import org.mockito.Mock
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
		return Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
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
	internal fun provideBase64Wrapper(): Base64Wrapper {
		return Base64Wrapper()
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

	/*@Provides
	@Singleton
	internal fun provideDataManager(companiesHouseService: CompaniesHouseService, companiesHouseDocumentService: CompaniesHouseDocumentService,
									@Mock preferencesHelper: PreferencesHelper, base64Wrapper: Base64Wrapper): CompaniesRepository {
		//val dataManager = Mockito.mock(CompaniesRepository::class.java)
		CompaniesRepository(companiesHouseService, companiesHouseDocumentService, preferencesHelper, base64Wrapper)
		val dataManager = mock<CompaniesRepository>()
		val searchHistoryItem = SearchHistoryItem("Acme Painting", "12345678", 12)
		val searchHistoryVisitables = arrayOf(searchHistoryItem)

		val preferencesHelper = mock<PreferencesHelper>()
		dataManager.preferencesHelper = preferencesHelper
		whenever(dataManager.favourites).thenReturn(searchHistoryVisitables)
		return dataManager
	}*/

	@Provides
	@Singleton
	internal fun provideDataManager(): CompaniesRepositoryContract {
		return AndroidTestCompaniesRepository()
	}

	/*@Provides
	internal fun provideFavouritesPresenter(companiesRepository: CompaniesRepository): FavouritesPresenter {
		return FavouritesPresenter(companiesRepository)
	}*/
}
