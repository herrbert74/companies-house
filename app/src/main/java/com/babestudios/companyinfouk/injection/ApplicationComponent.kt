package com.babestudios.companyinfouk.injection

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.ui.charges.ChargesPresenter
import com.babestudios.companyinfouk.ui.company.CompanyPresenter
import com.babestudios.companyinfouk.ui.favourites.FavouritesActivity
import com.babestudios.companyinfouk.ui.filinghistory.list.FilingHistoryAdapter
import com.babestudios.companyinfouk.ui.filinghistory.FilingHistoryPresenter
import com.babestudios.companyinfouk.ui.filinghistory.list.FilingHistoryViewHolder
import com.babestudios.companyinfouk.ui.filinghistorydetails.FilingHistoryDetailsActivity
import com.babestudios.companyinfouk.ui.insolvency.InsolvencyActivity
import com.babestudios.companyinfouk.ui.officerappointments.OfficerAppointmentsActivity
import com.babestudios.companyinfouk.ui.officerdetails.OfficerDetailsActivity
import com.babestudios.companyinfouk.ui.officers.OfficersActivity
import com.babestudios.companyinfouk.ui.persons.PersonsActivity
import com.babestudios.companyinfouk.ui.search.RecentSearchesResultsAdapter
import com.babestudios.companyinfouk.ui.search.SearchActivity
import com.babestudios.companyinfouk.ui.search.SearchPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
	fun inject(searchPresenter: SearchPresenter)

	fun inject(recentSearchesResultsAdapter: RecentSearchesResultsAdapter)

	fun inject(companiesHouseApplication: CompaniesHouseApplication)

	fun inject(searchActivity: SearchActivity)

	fun inject(insolvencyActivity: InsolvencyActivity)

	fun inject(officersActivity: OfficersActivity)

	fun inject(personsActivity: PersonsActivity)

	fun inject(filingHistoryDetailsActivity: FilingHistoryDetailsActivity)

	fun inject(officerDetailsActivity: OfficerDetailsActivity)

	fun inject(officerAppointmentsActivity: OfficerAppointmentsActivity)

	fun inject(favouritesActivity: FavouritesActivity)

	fun inject(filingHistoryAdapter: FilingHistoryAdapter)

	fun inject(filingHistoryViewHolder: FilingHistoryViewHolder)

	fun inject(filingHistoryPresenter: FilingHistoryPresenter)

	fun filingHistoryPresenter(): FilingHistoryPresenter

	fun chargesPresenter(): ChargesPresenter

	fun companyPresenter(): CompanyPresenter

	/*@Named("CompaniesHouseRetrofit")
	Retrofit getCompaniesHouseRetrofit();

	CompaniesHouseService getCompaniesHouseService();*/

	/*Application application();
	@ApplicationContext Context context();
	CompaniesRepository companiesRepository();*/

}
