package com.babestudios.companyinfouk.companies.ui.company

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore.Intent
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore.State
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CompanyExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadCompany -> fetchCompany(action.companyId)
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			Intent.FabFavouritesClicked ->
				flipCompanyFavoriteState(getState().companyId, getState().company.companyName)
		}
	}

	//region company actions

	private fun fetchCompany(companyId: String) {
		scope.launch(ioContext) {
			val company = companiesRepository.getCompany(companyId)
			val isFavourite = companiesRepository.isFavourite(
				SearchHistoryItem(
					company.companyName,
					companyId,
					0L
				)
			)
			withContext(mainContext) {
				dispatch(Message.CompanyLoaded(company, isFavourite))
			}
		}
	}

	private fun flipCompanyFavoriteState(companyNumber: String, companyName: String) {
		scope.launch(ioContext) {
			val isFavouriteAfterFlip =
				if (companiesRepository.isFavourite(SearchHistoryItem(companyName, companyNumber, 0))) {
					companiesRepository.removeFavourite(SearchHistoryItem(companyName, companyNumber, 0))
					false
				} else {
					companiesRepository.addFavourite(SearchHistoryItem(companyName, companyNumber, 0))
					true
				}
			withContext(mainContext) {
				dispatch(Message.FlipFavourite(isFavouriteAfterFlip))
			}
		}
	}

	//endregion

}
