package com.babestudios.companyinfouk.injection;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.ui.charges.ChargesActivity;
import com.babestudios.companyinfouk.ui.company.CompanyActivity;
import com.babestudios.companyinfouk.ui.favourites.FavouritesActivity;
import com.babestudios.companyinfouk.ui.filinghistory.FilingHistoryActivity;
import com.babestudios.companyinfouk.ui.filinghistorydetails.FilingHistoryDetailsActivity;
import com.babestudios.companyinfouk.ui.insolvency.InsolvencyActivity;
import com.babestudios.companyinfouk.ui.officerappointments.OfficerAppointmentsActivity;
import com.babestudios.companyinfouk.ui.officerdetails.OfficerDetailsActivity;
import com.babestudios.companyinfouk.ui.officers.OfficersActivity;
import com.babestudios.companyinfouk.ui.persons.PersonsActivity;
import com.babestudios.companyinfouk.ui.search.RecentSearchesResultsAdapter;
import com.babestudios.companyinfouk.ui.search.SearchActivity;
import com.babestudios.companyinfouk.ui.search.SearchPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
	void inject(SearchPresenter searchPresenter);

	void inject(RecentSearchesResultsAdapter recentSearchesResultsAdapter);

	void inject(CompaniesHouseApplication companiesHouseApplication);

	void inject(SearchActivity searchActivity);

	void inject(FilingHistoryActivity filingHistoryActivity);

	void inject(ChargesActivity chargesActivity);

	void inject(InsolvencyActivity insolvencyActivity);

	void inject(OfficersActivity officersActivity);

	void inject(PersonsActivity personsActivity);

	void inject(FilingHistoryDetailsActivity filingHistoryDetailsActivity);

	void inject(OfficerDetailsActivity officerDetailsActivity);

	void inject(OfficerAppointmentsActivity officerAppointmentsActivity);

	void inject(CompanyActivity companyActivity);

	void inject(FavouritesActivity favouritesActivity);

	/*@Named("CompaniesHouseRetrofit")
	Retrofit getCompaniesHouseRetrofit();

	CompaniesHouseService getCompaniesHouseService();*/

	/*Application application();
	@ApplicationContext Context context();
	DataManager dataManager();*/

}
