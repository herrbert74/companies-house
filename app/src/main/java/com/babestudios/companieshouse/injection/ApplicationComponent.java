package com.babestudios.companieshouse.injection;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.ui.charges.ChargesActivity;
import com.babestudios.companieshouse.ui.company.CompanyPresenter;
import com.babestudios.companieshouse.ui.favourites.FavouritesPresenter;
import com.babestudios.companieshouse.ui.filinghistory.FilingHistoryActivity;
import com.babestudios.companieshouse.ui.filinghistorydetails.FilingHistoryDetailsActivity;
import com.babestudios.companieshouse.ui.insolvency.InsolvencyActivity;
import com.babestudios.companieshouse.ui.officers.OfficersActivity;
import com.babestudios.companieshouse.ui.persons.PersonsActivity;
import com.babestudios.companieshouse.ui.search.RecentSearchesResultsAdapter;
import com.babestudios.companieshouse.ui.search.SearchActivity;
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

	void inject(SearchActivity searchActivity);

	void inject(FilingHistoryActivity filingHistoryActivity);

	void inject(ChargesActivity chargesActivity);

	void inject(InsolvencyActivity insolvencyActivity);

	void inject(OfficersActivity officersActivity);

	void inject(PersonsActivity personsActivity);

	void inject(FilingHistoryDetailsActivity filingHistoryDetailsActivity);

	/*@Named("CompaniesHouseRetrofit")
	Retrofit getCompaniesHouseRetrofit();

	CompaniesHouseService getCompaniesHouseService();*/

	/*Application application();
	@ApplicationContext Context context();
	DataManager dataManager();*/

}
