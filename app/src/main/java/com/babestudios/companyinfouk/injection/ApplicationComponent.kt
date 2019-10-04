package com.babestudios.companyinfouk.injection

import android.content.Context
import com.babestudios.base.di.scope.ApplicationScope
import com.babestudios.companyinfo.core.injection.CoreComponent
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.ui.charges.ChargesPresenter
import com.babestudios.companyinfouk.ui.company.CompanyPresenter
import com.babestudios.companyinfouk.ui.favourites.FavouritesPresenter
import com.babestudios.companyinfouk.ui.filinghistory.FilingHistoryPresenter
import com.babestudios.companyinfouk.ui.filinghistory.list.FilingHistoryAdapter
import com.babestudios.companyinfouk.ui.filinghistory.list.FilingHistoryViewHolder
import com.babestudios.companyinfouk.ui.filinghistorydetails.FilingHistoryDetailsPresenter
import com.babestudios.companyinfouk.ui.insolvency.InsolvencyPresenter
import com.babestudios.companyinfouk.ui.insolvencydetails.InsolvencyDetailsPresenter
import com.babestudios.companyinfouk.ui.officerappointments.OfficerAppointmentsPresenter
import com.babestudios.companyinfouk.ui.officerdetails.OfficerDetailsPresenter
import com.babestudios.companyinfouk.ui.officers.OfficersPresenter
import com.babestudios.companyinfouk.ui.persondetails.PersonDetailsPresenter
import com.babestudios.companyinfouk.ui.persons.PersonsPresenter
import com.babestudios.companyinfouk.ui.main.MainPresenter
import dagger.Component

@ApplicationScope
@Component(/*modules = [ApplicationModule::class]*/ dependencies = [CoreComponent::class])
interface ApplicationComponent {

//	fun context(): Context

	/*fun companyPresenter(): CompanyPresenter

	fun favouritesPresenter(): FavouritesPresenter

	fun filingHistoryDetailsPresenter(): FilingHistoryDetailsPresenter

	fun insolvencyPresenter(): InsolvencyPresenter

	fun insolvencyDetailsPresenter(): InsolvencyDetailsPresenter

	fun officerAppointmentsPresenter(): OfficerAppointmentsPresenter

	fun officerDetailsPresenter(): OfficerDetailsPresenter

	fun officersPresenter(): OfficersPresenter

	fun personDetailsPresenter(): PersonDetailsPresenter

	fun personsPresenter(): PersonsPresenter

	fun mainPresenter(): MainPresenter*/

}
