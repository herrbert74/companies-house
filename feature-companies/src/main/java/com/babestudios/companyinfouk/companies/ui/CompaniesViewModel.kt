package com.babestudios.companyinfouk.companies.ui

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.navigation.features.CompaniesNavigator

class CompaniesViewModel(
		companiesState: CompaniesState,
		private val companiesRepository: CompaniesRepositoryContract,
		val companiesNavigator: CompaniesNavigator,
		private val errorResolver: ErrorResolver
) : BaseViewModel<CompaniesState>(companiesState, companiesRepository) {

	companion object : MvRxViewModelFactory<CompaniesViewModel, CompaniesState> {

		@JvmStatic
		override fun create(viewModelContext: ViewModelContext, state: CompaniesState): CompaniesViewModel? {
			val companiesRepository = viewModelContext.activity<CompaniesActivity>().injectCompaniesHouseRepository()
			val companiesNavigator = viewModelContext.activity<CompaniesActivity>().injectCompaniesNavigator()
			val errorResolver = viewModelContext.activity<CompaniesActivity>().injectErrorResolver()
			return CompaniesViewModel(
					state,
					companiesRepository,
					companiesNavigator,
					errorResolver
			)
		}
	}
}
