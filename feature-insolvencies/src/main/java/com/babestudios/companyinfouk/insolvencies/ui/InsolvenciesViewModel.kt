package com.babestudios.companyinfouk.insolvencies.ui

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.base.mvrx.resolveErrorOrProceed
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.ui.details.list.*
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.AbstractInsolvencyVisitable
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvencyVisitable
import com.babestudios.companyinfouk.navigation.features.InsolvenciesNavigator

class InsolvenciesViewModel(
		insolvenciesState: InsolvenciesState,
		private val companiesRepository: CompaniesRepositoryContract,
		val insolvenciesNavigator: InsolvenciesNavigator,
		private val errorResolver: ErrorResolver,
		private val datesTitleString:String,
		private val practitionersTitleString:String
) : BaseViewModel<InsolvenciesState>(insolvenciesState, companiesRepository) {

	init {
		withState {
			fetchInsolvencies(it.companyNumber)
		}
	}
	companion object : MvRxViewModelFactory<InsolvenciesViewModel, InsolvenciesState> {

		@JvmStatic
		override fun create(viewModelContext: ViewModelContext, state: InsolvenciesState): InsolvenciesViewModel? {
			val companiesRepository = viewModelContext.activity<InsolvenciesActivity>().injectCompaniesHouseRepository()
			val insolvenciesNavigator = viewModelContext.activity<InsolvenciesActivity>().injectInsolvenciesNavigator()
			val errorResolver = viewModelContext.activity<InsolvenciesActivity>().injectErrorResolver()
			val datesTitleString = viewModelContext.activity<InsolvenciesActivity>().injectDatesTitleString()
			val practitionersTitleString = viewModelContext.activity<InsolvenciesActivity>()
					.injectPractitionersTitleString()
			return InsolvenciesViewModel(
					state,
					companiesRepository,
					insolvenciesNavigator,
					errorResolver,
					datesTitleString,
					practitionersTitleString
			)
		}

		override fun initialState(viewModelContext: ViewModelContext): InsolvenciesState? {
			val companyNumber = viewModelContext.activity<InsolvenciesActivity>().provideCompanyNumber()
			return InsolvenciesState(companyNumber = companyNumber)
		}
	}

	//region charges

	private fun fetchInsolvencies(companyNumber: String) {
		companiesRepository.getInsolvency(companyNumber)
				.execute {
					copy(
							insolvencyRequest = it.resolveErrorOrProceed(errorResolver),
							insolvencies = convertToVisitables(it())
					)
				}
	}

	private fun convertToVisitables(reply: Insolvency?): List<AbstractInsolvencyVisitable> {
		return ArrayList(reply?.cases?.map { item -> InsolvencyVisitable(item) } ?: emptyList())
	}

	fun insolvencyItemClicked(adapterPosition: Int) {
		withState { state ->
			val newCase = (state.insolvencies[adapterPosition] as InsolvencyVisitable).insolvencyCase
			setState {
				copy(
						insolvencyCase = newCase,
						insolvencyDetailsItems = convertToVisitables(newCase)
				)
			}
		}
		insolvenciesNavigator.insolvenciesToInsolvencyDetails()
	}

	private fun convertToVisitables(insolvencyCase: InsolvencyCase): List<AbstractInsolvencyDetailsVisitable> {
		val list = ArrayList<AbstractInsolvencyDetailsVisitable>()
		list.add(InsolvencyDetailsTitleVisitable(InsolvencyDetailsTitleItem(datesTitleString)))
		for (item in insolvencyCase.dates) {
			list.add(InsolvencyDetailsDateVisitable(InsolvencyDetailsDateItem(item.date, item.type)))
		}
		list.add(InsolvencyDetailsTitleVisitable(InsolvencyDetailsTitleItem(practitionersTitleString)))
		for (item in insolvencyCase.practitioners) {
			list.add(InsolvencyDetailsPractitionerVisitable(InsolvencyDetailsPractitionerItem(item)))
		}
		return list
	}

	//endregion
}
