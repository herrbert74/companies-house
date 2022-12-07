package com.babestudios.companyinfouk.persons.ui.persons

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.Intent
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonsExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadPersons -> {
				companiesRepository.logScreenView("PersonsFragment")
				fetchPersons(action.companyNumber)
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.LoadMorePersons -> loadMorePersons(getState)
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


	private fun loadMorePersons(getState: () -> State) {
		val showState = (getState() as State.Show)
		if (showState.personsResponse.items.size < showState.personsResponse.totalResults) {
			scope.launch {
				val personsResponse = companiesRepository.getPersons(
					showState.companyNumber,
					(showState.personsResponse.items.size).toString()
				)
				dispatch(Message.LoadMorePersonsMessage(personsResponse, showState.companyNumber))
			}
		}
	}

	//endregion

}
