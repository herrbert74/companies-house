package com.babestudios.companieshouse.injection;

import android.app.Application;
import android.content.Context;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.ui.company.CompanyPresenter;
import com.babestudios.companieshouse.ui.favourites.FavouritesPresenter;
import com.babestudios.companieshouse.ui.search.RecentSearchesResultsAdapter;
import com.babestudios.companieshouse.ui.search.SearchPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
	void inject(SearchPresenter searchPresenter);
	void inject(CompanyPresenter companyPresenter);
	void inject(FavouritesPresenter favouritesPresenter);
	void inject(RecentSearchesResultsAdapter recentSearchesResultsAdapter);

	void inject(CompaniesHouseApplication companiesHouseApplication);

	/*@Named("CompaniesHouseRetrofit")
	Retrofit getCompaniesHouseRetrofit();

	CompaniesHouseService getCompaniesHouseService();*/

	Application application();
	@ApplicationContext Context context();
	DataManager dataManager();

}
