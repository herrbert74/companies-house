package com.babestudios.companyinfouk.persons.ui.persons

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.Intent
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class PersonsExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

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
			is Intent.PersonsItemClicked -> personsItemClicked(intent.selectedPerson)
			is Intent.LoadMorePersons -> loadMorePersons(getState, intent.page)
		}
	}

	//region officers

	private fun fetchPersons(companyNumber: String) {
		scope.launch {
			val personsResponse = companiesRepository.getPersons(companyNumber, "0")
			dispatch(Message.PersonsMessage(personsResponse, companyNumber))
		}
	}


	private fun loadMorePersons(getState: () -> State, page: Int) {
		val showState = (getState() as State.Show)
		if (showState.personsResponse.items.size < showState.personsResponse.totalResults) {
			scope.launch {
				val personsResponse = companiesRepository.getPersons(
					showState.companyNumber,
					(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString()
				)
				dispatch(Message.PersonsMessage(personsResponse, showState.companyNumber))
			}
		}
	}

	private fun personsItemClicked(person: Person) {
		scope.launch {
			publish(SideEffect.PersonsItemClicked(person))
		}
	}

//endregion

}
