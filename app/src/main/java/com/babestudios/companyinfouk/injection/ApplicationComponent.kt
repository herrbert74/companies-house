package com.babestudios.companyinfouk.injection

import com.babestudios.base.di.scope.ApplicationScope
import com.babestudios.companyinfouk.core.injection.CoreComponent
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
