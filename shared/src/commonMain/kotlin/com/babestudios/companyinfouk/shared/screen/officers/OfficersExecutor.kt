package com.babestudios.companyinfouk.shared.screen.officers

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.screen.officers.OfficersStore.Intent
import com.babestudios.companyinfouk.shared.screen.officers.OfficersStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfficersExecutor(
	private val companiesRepository: CompaniesRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

	override fun executeAction(action: BootstrapIntent) {
		when (action) {
			is BootstrapIntent.LoadOfficers -> {
				companiesRepository.logScreenView("OfficersFragment")
				fetchOfficers(action.companyId)
			}
		}
	}

	override fun executeIntent(intent: Intent) {
		when (intent) {
			is Intent.LoadMoreOfficers -> loadMoreOfficers(state())
		}
	}

	//region officers actions

	private fun fetchOfficers(companyId: String) {
		scope.launch(ioContext) {
			val officersResponse = companiesRepository.getOfficers(companyId, "0")
			withContext(mainContext) {
				dispatch(Message.OfficersMessage(officersResponse, companyId))
			}
		}
	}

	private fun loadMoreOfficers(state: State) {
		if (state.officersResponse.items.size < state.officersResponse.totalResults) {
			scope.launch {
				val officersResponse = companiesRepository.getOfficers(
					state.companyId,
					(state.officersResponse.items.size).toString()
				)
				dispatch(Message.LoadMoreOfficersMessage(officersResponse, state.companyId))
			}
		}
	}

	//endregion

}
