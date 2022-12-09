package com.babestudios.companyinfouk.officers.ui.officers

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.Intent
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfficersExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadOfficers -> {
				companiesRepository.logScreenView("OfficersFragment")
				fetchOfficers(action.companyId)
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.LoadMoreOfficers -> loadMoreOfficers(getState)
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


	private fun loadMoreOfficers(getState: () -> State) {
		val showState = getState()
		if (showState.officersResponse.items.size < showState.officersResponse.totalResults) {
			scope.launch {
				val officersResponse = companiesRepository.getOfficers(
					showState.companyId,
					(showState.officersResponse.items.size).toString()
				)
				dispatch(Message.LoadMoreOfficersMessage(officersResponse, showState.companyId))
			}
		}
	}

	//endregion

}
