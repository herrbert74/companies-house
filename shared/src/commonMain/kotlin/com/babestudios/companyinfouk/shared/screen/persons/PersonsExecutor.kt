package com.babestudios.companyinfouk.shared.screen.persons

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.screen.persons.PersonsStore.Intent
import com.babestudios.companyinfouk.shared.screen.persons.PersonsStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonsExecutor(
	private val companiesRepository: CompaniesRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

	override fun executeAction(action: BootstrapIntent) {
		when (action) {
			is BootstrapIntent.LoadPersons -> {
				companiesRepository.logScreenView("PersonsFragment")
				fetchPersons(action.companyNumber)
			}
		}
	}

	override fun executeIntent(intent: Intent) {
		when (intent) {
			is Intent.LoadMorePersons -> loadMorePersons(state())
		}
	}

	//region persons actions

	private fun fetchPersons(companyNumber: String) {
		scope.launch(ioContext) {
			val personsResponse = companiesRepository.getPersons(companyNumber, "0")
			withContext(mainContext) {
				dispatch(Message.PersonsMessage(personsResponse, companyNumber))
			}
		}
	}


	private fun loadMorePersons(state: State) {
		if (state.personsResponse.items.size < state.personsResponse.totalResults) {
			scope.launch {
				val personsResponse = companiesRepository.getPersons(
					state.companyId,
					(state.personsResponse.items.size).toString()
				)
				dispatch(Message.LoadMorePersonsMessage(personsResponse, state.companyId))
			}
		}
	}

	//endregion

}
