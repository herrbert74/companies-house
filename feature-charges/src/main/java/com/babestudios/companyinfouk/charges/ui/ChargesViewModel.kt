package com.babestudios.companyinfouk.charges.ui

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.appendAt
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.base.mvrx.resolveErrorOrProceed
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.charges.ui.charges.list.AbstractChargesVisitable
import com.babestudios.companyinfouk.charges.ui.charges.list.ChargesVisitable
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.charges.Charges
import com.babestudios.companyinfouk.navigation.features.ChargesNavigator

class ChargesViewModel(
		chargesState: ChargesState,
		private val companiesRepository: CompaniesRepositoryContract,
		val chargesNavigator: ChargesNavigator,
		private val errorResolver: ErrorResolver
) : BaseViewModel<ChargesState>(chargesState, companiesRepository) {

	companion object : MvRxViewModelFactory<ChargesViewModel, ChargesState> {

		@JvmStatic
		override fun create(viewModelContext: ViewModelContext, state: ChargesState): ChargesViewModel? {
			val companiesRepository = viewModelContext.activity<ChargesActivity>().injectCompaniesHouseRepository()
			val chargesNavigator = viewModelContext.activity<ChargesActivity>().injectChargesNavigator()
			val errorResolver = viewModelContext.activity<ChargesActivity>().injectErrorResolver()
			return ChargesViewModel(
					state,
					companiesRepository,
					chargesNavigator,
					errorResolver
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

	//region charges

	fun fetchCharges(companyNumber: String) {
		companiesRepository.fetchCharges(companyNumber, "0")
				.execute {
					copy(
							chargesRequest = it.resolveErrorOrProceed(errorResolver),
							charges = convertToVisitables(it()),
							totalChargesCount = it()?.totalCount?.toInt()?: 0
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
							chargesRequest = it.resolveErrorOrProceed(errorResolver),
							charges = charges.appendAt(convertToVisitables(it()), charges.size + 1),
							totalChargesCount = it()?.totalCount?.toInt() ?: 0
					)
				}
			}
		}
	}

	private fun convertToVisitables(reply: Charges?): List<AbstractChargesVisitable> {
		return ArrayList(reply?.items?.map { item -> ChargesVisitable(item) } ?: emptyList())
	}

	fun chargesItemClicked(adapterPosition: Int) {
		withState { state ->
			setState {
				copy(
						chargesItem = (state.charges[adapterPosition] as ChargesVisitable).chargesItem
				)
			}
		}
		chargesNavigator.chargesToChargesDetails()
	}

	//endregion
}
