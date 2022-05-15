package com.babestudios.companyinfouk.officers.ui.officers

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.data.BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.Intent
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.State
import java.util.regex.Pattern
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class OfficersExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadOfficers -> fetchOfficers(action.companyNumber)
		}
	}

	@Suppress("ComplexMethod")
	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.LoadMoreOfficers -> loadMoreOfficers(getState, intent.page)
			is Intent.OfficerItemClicked -> officerItemClicked(intent.selectedOfficer)
		}
	}

	//region Main intents

	//region officers

	private fun fetchOfficers(companyNumber: String) {
		scope.launch {
			val officersResponse = companiesRepository.getOfficers(companyNumber, "0")
			dispatch(Message.OfficersMessage(officersResponse, companyNumber))
		}
	}


	private fun loadMoreOfficers(getState: () -> State, page: Int) {
		val showState = (getState() as State.Show)
		if (showState.officers.size < showState.totalOfficersCount) {
			scope.launch {
				val officersResponse = companiesRepository.getOfficers(
					showState.companyNumber,
					(page * Integer.valueOf(COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString()
				)
				dispatch(Message.OfficersMessage(officersResponse, showState.companyNumber))
			}
		}
	}

	private fun officerItemClicked(officer: Officer) {
		val pattern = Pattern.compile("officers/(.+)/appointments")
		val matcher = pattern.matcher(officer.links.officer?.appointments ?: "")
		var officerId = ""
		if (matcher.find()) {
			officerId = matcher.group(1) ?: ""
		}
		scope.launch {
			publish(SideEffect.OfficerItemClicked(officer, officerId))
		}
	}

//endregion

}
