package com.babestudios.companyinfouk.charges.ui.charges

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore.Intent
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore.State
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.charges.ChargesItem
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class ChargesExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadCharges -> {
				companiesRepository.logScreenView("PersonsFragment")
				fetchCharges(action.companyNumber)
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.ChargesItemClicked -> chargesItemClicked(intent.selectedChargesItem)
			is Intent.LoadMoreCharges -> loadMoreCharges(getState, intent.page)
		}
	}

	//region charges

	private fun fetchCharges(companyNumber: String) {
		scope.launch {
			val charges = companiesRepository.getCharges(companyNumber, "0")
			dispatch(Message.ChargesMessage(charges, companyNumber))
		}
	}


	private fun loadMoreCharges(getState: () -> State, page: Int) {
		val showState = (getState() as State.Show)
		if (showState.charges.items.size < showState.charges.totalCount) {
			scope.launch {
				val personsResponse = companiesRepository.getCharges(
					showState.companyNumber,
					(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString()
				)
				dispatch(Message.ChargesMessage(personsResponse, showState.companyNumber))
			}
		}
	}

	private fun chargesItemClicked(chargesItem: ChargesItem) {
		scope.launch {
			publish(SideEffect.ChargesItemClicked(chargesItem))
		}
	}

	//endregion

}
