package com.babestudios.companyinfouk.shared.screen.charges

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.shared.screen.charges.ChargesStore.Intent
import com.babestudios.companyinfouk.shared.screen.charges.ChargesStore.State
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChargesExecutor constructor(
	private val companiesRepository: CompaniesRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadCharges -> {
				companiesRepository.logScreenView("ChargesFragment")
				fetchCharges(action.selectedCompanyId)
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.LoadMoreCharges -> loadMoreCharges(getState)
		}
	}

	//region charges actions

	private fun fetchCharges(selectedCompanyId: String) {
		scope.launch(ioContext) {
			val chargesResponse = companiesRepository.getCharges(selectedCompanyId, "0")
			withContext(mainContext) {
				dispatch(Message.ChargesMessage(chargesResponse, selectedCompanyId))
			}
		}
	}

	private fun loadMoreCharges(getState: () -> State) {
		val showState = getState()
		if (showState.chargesResponse.items.size < showState.chargesResponse.totalCount) {
			scope.launch {
				val chargesResponse = companiesRepository.getCharges(
					showState.selectedCompanyId,
					(showState.chargesResponse.items.size).toString()
				)
				dispatch(Message.LoadMoreChargesMessage(chargesResponse, showState.selectedCompanyId))
			}
		}
	}

	//endregion

}
