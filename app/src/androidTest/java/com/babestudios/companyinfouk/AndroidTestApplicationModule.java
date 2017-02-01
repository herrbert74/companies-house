package com.babestudios.companyinfouk;

import android.app.Application;
import android.content.Context;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.local.PreferencesHelper;
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService;
import com.babestudios.companyinfouk.data.network.CompaniesHouseService;
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory;
import com.babestudios.companyinfouk.injection.ApplicationContext;
import com.babestudios.companyinfouk.ui.favourites.FavouritesPresenter;
import com.babestudios.companyinfouk.utils.Base64Wrapper;

import org.mockito.Mockito;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static org.mockito.Mockito.when;

@Module
public class AndroidTestApplicationModule {

	private Application application;

	public AndroidTestApplicationModule(Application application) {
		this.application = application;
	}

	@Provides
	@Singleton
	@Named("CompaniesHouseRetrofit")
	Retrofit provideCompaniesHouseRetrofit() {
		return new Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.build();
	}

	@Provides
	@Singleton
	CompaniesHouseService provideCompaniesHouseService(@Named("CompaniesHouseRetrofit") Retrofit retroFit) {
		return retroFit.create(CompaniesHouseService.class);
	}

	@Provides
	@Singleton
	CompaniesHouseDocumentService provideCompaniesHouseDocumentService(@Named("CompaniesHouseRetrofit") Retrofit retroFit) {
		return retroFit.create(CompaniesHouseDocumentService.class);
	}

	@Provides
	Base64Wrapper provideBase64Wrapper() {
		return new Base64Wrapper();
	}

	@Provides
	Application provideApplication() {
		return application;
	}

	@Provides
	@ApplicationContext
	Context provideContext() {
		return application;
	}

	@Provides
	@Singleton
	DataManager provideDataManager(CompaniesHouseService companiesHouseService, CompaniesHouseDocumentService companiesHouseDocumentService, PreferencesHelper preferencesHelper, Base64Wrapper base64Wrapper) {
		DataManager dataManager = Mockito.mock(DataManager.class);
		SearchHistoryItem[] searchHistoryItems = new SearchHistoryItem[1];
		SearchHistoryItem searchHistoryItem = new SearchHistoryItem("Acme Painting", "12345678", 12);
		searchHistoryItems[0] = searchHistoryItem;
		when(dataManager.getFavourites()).thenReturn(searchHistoryItems);
		return dataManager;
	}

	@Provides
	FavouritesPresenter provideFavouritesPresenter(DataManager dataManager) {
		return new FavouritesPresenter(dataManager);
	}
}
