package com.babestudios.companyinfouk.insolvencies.ui

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.companyinfouk.common.model.insolvency.Insolvency
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.insolvencies.ui.details.list.*
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvencyVisitable
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvencyVisitableBase
import com.babestudios.companyinfouk.navigation.features.InsolvenciesNavigator

class InsolvenciesViewModel(
		insolvenciesState: InsolvenciesState,
		private val companiesRepository: CompaniesRepositoryContract,
		var insolvenciesNavigator: InsolvenciesNavigator,
		private val datesTitleString: String,
		private val practitionersTitleString: String
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
			val datesTitleString = viewModelContext.activity<InsolvenciesActivity>().injectDatesTitleString()
			val practitionersTitleString = viewModelContext.activity<InsolvenciesActivity>()
					.injectPractitionersTitleString()
			return InsolvenciesViewModel(
					state,
					companiesRepository,
					insolvenciesNavigator,
					datesTitleString,
					practitionersTitleString
			)
		}

		override fun initialState(viewModelContext: ViewModelContext): InsolvenciesState? {
			val companyNumber = viewModelContext.activity<InsolvenciesActivity>().provideCompanyNumber()
			return if (companyNumber.isNotEmpty())
				InsolvenciesState(companyNumber = companyNumber)
			else
				null
		}
	}

	fun setNavigator(navigator: InsolvenciesNavigator) {
		insolvenciesNavigator = navigator
	}

	//region charges

	private fun fetchInsolvencies(companyNumber: String) {
		companiesRepository.getInsolvency(companyNumber)
				.execute {
					copy(
							insolvencyRequest = it,
							insolvencies = convertInsolvencyToVisitables(it())
					)
				}
	}

	private fun convertInsolvencyToVisitables(reply: Insolvency?): List<InsolvencyVisitableBase> {
		return ArrayList(reply?.cases?.map { item -> InsolvencyVisitable(item) } ?: emptyList())
	}

	fun insolvencyItemClicked(adapterPosition: Int) {
		withState { state ->
			val newCase = (state.insolvencies[adapterPosition] as InsolvencyVisitable).insolvencyCase
			setState {
				copy(insolvencyCase = newCase)
			}
		}
		insolvenciesNavigator.insolvenciesToInsolvencyDetails()
	}

	/**
	/ The list of insolvency cases is shown on the main screen, and dates/practitioners are shown on the details screen
	 **/
	fun convertCaseToDetailsVisitables() {
		val list = ArrayList<InsolvencyDetailsVisitableBase>()
		withState {
			list.add(InsolvencyDetailsTitleVisitable(InsolvencyDetailsTitleItem(datesTitleString)))
			for (item in it.insolvencyCase.dates) {
				list.add(InsolvencyDetailsDateVisitable(InsolvencyDetailsDateItem(item.date, item.type)))
			}
			list.add(InsolvencyDetailsTitleVisitable(InsolvencyDetailsTitleItem(practitionersTitleString)))
			for (item in it.insolvencyCase.practitioners) {
				list.add(InsolvencyDetailsPractitionerVisitable(InsolvencyDetailsPractitionerItem(item)))
			}
			setState {
				copy(insolvencyDetailsItems = list.toList())
			}
		}
	}

	fun practitionerClicked(adapterPosition: Int) {
		withState { state ->
			val practitioner = (state.insolvencyDetailsItems[adapterPosition] as InsolvencyDetailsPractitionerVisitable)
					.insolvencyDetailsPractitionerItem.practitioner
			setState { copy(selectedPractitioner = practitioner) }
		}
		insolvenciesNavigator.insolvencyDetailsToPractitioner()
	}

	//endregion
}
