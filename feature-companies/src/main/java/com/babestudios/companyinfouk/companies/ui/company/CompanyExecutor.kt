package com.babestudios.companyinfouk.companies.ui.company

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore.Intent
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore.State
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.common.getAddressString
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CompanyExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.FetchCompany -> {
				companiesRepository.logScreenView("Company")
				fetchCompany(action.companyNumber)
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		val showState = (getState() as State.Show)
		when (intent) {
			is Intent.FabFavouritesClicked ->
				flipCompanyFavoriteState(showState.companyNumber, showState.company.companyName)

			is Intent.MapClicked ->
				publish(SideEffect.MapClicked(showState.company.registeredOfficeAddress.getAddressString()))
		}
	}

	private fun fetchCompany(companyNumber: String) {
		scope.launch(ioContext) {
			val company = companiesRepository.getCompany(companyNumber)
			val isFavourite = companiesRepository.isFavourite(
				SearchHistoryItem(
					company.companyName,
					companyNumber,
					0
				)
			)
			withContext(mainContext) {
				dispatch(Message.CompanyLoaded(company, isFavourite))
			}
		}
	}

	private fun flipCompanyFavoriteState(companyNumber: String, companyName: String) {
		scope.launch(ioContext) {
			if (companiesRepository.isFavourite(SearchHistoryItem(companyName, companyNumber, 0))) {
				companiesRepository.removeFavourite(SearchHistoryItem(companyName, companyNumber, 0))
			} else {
				companiesRepository.addFavourite(SearchHistoryItem(companyName, companyNumber, 0))
			}
			val isFavourite = companiesRepository.isFavourite(
				SearchHistoryItem(companyName, companyNumber, 0)
			)
			withContext(mainContext) {
				dispatch(Message.FlipFavourite(isFavourite))
			}
		}
	}
}
