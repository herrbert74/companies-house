package com.babestudios.companyinfouk.charges.ui

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.appendAt
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.companyinfouk.charges.ui.charges.list.ChargesVisitable
import com.babestudios.companyinfouk.charges.ui.charges.list.ChargesVisitableBase
import com.babestudios.companyinfouk.common.model.charges.Charges
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.navigation.features.ChargesNavigator

class ChargesViewModel(
		chargesState: ChargesState,
		private val companiesRepository: CompaniesRepositoryContract,
		var chargesNavigator: ChargesNavigator
) : BaseViewModel<ChargesState>(chargesState, companiesRepository) {

	companion object : MvRxViewModelFactory<ChargesViewModel, ChargesState> {

		@JvmStatic
		override fun create(viewModelContext: ViewModelContext, state: ChargesState): ChargesViewModel? {
			val companiesRepository = viewModelContext.activity<ChargesActivity>().injectCompaniesHouseRepository()
			val chargesNavigator = viewModelContext.activity<ChargesActivity>().injectChargesNavigator()
			return ChargesViewModel(
					state,
					companiesRepository,
					chargesNavigator
			)
		}

		override fun initialState(viewModelContext: ViewModelContext): ChargesState? {
			val companyNumber = viewModelContext.activity<ChargesActivity>().provideCompanyNumber()
			return if (companyNumber.isNotEmpty())
				ChargesState(companyNumber = companyNumber)
			else
				null
		}
	}

	fun setNavigator(navigator: ChargesNavigator) {
		chargesNavigator = navigator
	}

	//region charges

	fun fetchCharges(companyNumber: String) {
		companiesRepository.fetchCharges(companyNumber, "0")
				.execute {
					copy(
							chargesRequest = it,
							charges = convertToVisitables(it()),
							totalChargesCount = it()?.totalCount ?: 0
					)
				}
	}


	fun loadMoreCharges(page: Int) {
		withState { state ->
			if (state.charges.size < state.totalChargesCount) {
				companiesRepository.fetchCharges(
						state.companyNumber,
						(page * Integer
								.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE))
								.toString()
				).execute {
					copy(
							chargesRequest = it,
							charges = charges.appendAt(convertToVisitables(it()), charges.size + 1),
							totalChargesCount = it()?.totalCount ?: 0
					)
				}
			}
		}
	}

	private fun convertToVisitables(reply: Charges?): List<ChargesVisitableBase> {
		return ArrayList(reply?.items?.map { item -> ChargesVisitable(item) } ?: emptyList())
	}

	fun chargesItemClicked(adapterPosition: Int) {
		withState { state ->
			setState {
				copy(chargesItem = (state.charges[adapterPosition] as ChargesVisitable).chargesItem)
			}
		}
		chargesNavigator.chargesToChargesDetails()
	}

	//endregion
}
