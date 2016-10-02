package com.babestudios.companieshouse.injection;

import android.app.Application;
import android.content.Context;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.network.CompaniesHouseService;
import com.babestudios.companieshouse.ui.company.CompanyPresenter;
import com.babestudios.companieshouse.ui.search.SearchPresenter;
import com.babestudios.companieshouse.ui.search.RecentSearchesResultsAdapter;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
	void inject(SearchPresenter searchPresenter);
	void inject(CompanyPresenter companyPresenter);
	void inject(RecentSearchesResultsAdapter recentSearchesResultsAdapter);

	void inject(CompaniesHouseApplication companiesHouseApplication);

	@Named("SearchCompaniesRetrofit")
	Retrofit getSearchCompaniesRetrofit();

	CompaniesHouseService getSearchCompaniesService();

	Application application();
	@ApplicationContext Context context();
	DataManager dataManager();

}
