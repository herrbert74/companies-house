package com.babestudios.companyinfouk.officers.ui

import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator

class OfficersViewModel(
		officersState: OfficersState,
		private val context: Context,
		private val companiesRepository: CompaniesRepositoryContract,
		val officersNavigator: OfficersNavigator?
): BaseViewModel<OfficersState>(officersState) {

	companion object : MvRxViewModelFactory<OfficersViewModel, OfficersState> {

		@JvmStatic
		override fun create(viewModelContext: ViewModelContext, state: OfficersState): OfficersViewModel? {
			val context = viewModelContext.activity<OfficersActivity>().injectContext()
			val companiesRepository = viewModelContext.activity<OfficersActivity>().injectCompaniesHouseRepository()
			val officersNavigator = viewModelContext.activity<OfficersActivity>().injectOfficersNavigator()
			return OfficersViewModel(
					state,
					context,
					companiesRepository,
					officersNavigator
			)
		}
	}


}