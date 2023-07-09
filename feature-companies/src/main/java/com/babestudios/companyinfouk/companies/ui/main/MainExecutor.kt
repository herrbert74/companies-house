package com.babestudios.companyinfouk.companies.ui.main

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.companies.ui.main.MainStore.Intent
import com.babestudios.companyinfouk.companies.ui.main.MainStore.SideEffect
import com.babestudios.companyinfouk.companies.ui.main.MainStore.State
import com.babestudios.companyinfouk.companies.ui.main.Message.MoreSearchResult
import com.babestudios.companyinfouk.companies.ui.main.Message.SearchResult
import com.babestudios.companyinfouk.companies.ui.main.Message.SetSearchMenuItemCollapsed
import com.babestudios.companyinfouk.companies.ui.main.Message.SetSearchMenuItemExpanded
import com.babestudios.companyinfouk.companies.ui.main.Message.ShowRecentSearches
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.github.michaelbull.result.Ok
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainExecutor constructor(
	private val companiesRepository: CompaniesRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.ShowRecentSearches -> {
				companiesRepository.logScreenView("Main")
				if (getState().searchQuery == null)
					showRecentSearches()
				else
					getState().searchQuery?.let { onSearchQueryChanged(it, getState) }
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			Intent.ShowRecentSearches -> showRecentSearches()

			Intent.ClearRecentSearchesClicked ->
				if (getState().searchHistoryItems.isNotEmpty()) publish(SideEffect.ShowDeleteRecentSearchesDialog)

			Intent.ClearRecentSearches -> clearRecentSearches()

			is Intent.SearchQueryChanged -> onSearchQueryChanged(intent.queryText, getState)
			is Intent.LoadMoreSearch -> loadMoreSearch(getState)
			is Intent.SearchItemClicked -> searchItemClicked(intent.name, intent.number)
			is Intent.SetFilterState -> dispatch(Message.SetFilterState(intent.filterState))

			is Intent.SetSearchMenuItemExpanded -> {
				if (getState().searchQuery == null)
					dispatch(SetSearchMenuItemExpanded)
			}

			is Intent.SetSearchMenuItemCollapsed -> dispatch(SetSearchMenuItemCollapsed)
		}
	}

	//region Recent searches

	private fun showRecentSearches() {
		scope.launch(ioContext) {
			val recentSearches = companiesRepository.recentSearches()
			val recentSearchesWithSearchTime = updateHistoryWithSearchTime(recentSearches)
			withContext(mainContext) {
				dispatch(ShowRecentSearches(recentSearchesWithSearchTime))
			}
		}
	}

	private fun updateHistoryWithSearchTime(reply: List<SearchHistoryItem>): List<SearchHistoryItem> {
		return reply.map { item -> item.copy(searchTime = System.currentTimeMillis()) }
	}

	private fun clearRecentSearches() {
		scope.launch(ioContext) {
			companiesRepository.clearAllRecentSearches()
			withContext(mainContext) {
				dispatch(ShowRecentSearches(emptyList()))
			}
		}
	}

	//endregion

	//region main actions

	//endregion

	//region Search

	private fun onSearchQueryChanged(queryText: String?, getState: () -> State) {
		when {
			isComingBackFromCompanyScreen(queryText, getState) || getState().searchQuery == null -> return
			queryText != null && queryText.length > 2 -> search(queryText)
			else -> {
				dispatch(
					SearchResult(
						searchQuery = queryText,
						searchResult = Ok(CompanySearchResult()),
					),
				)
			}
		}
	}

	private fun isComingBackFromCompanyScreen(newQueryText: String?, getState: () -> State) =
		getState().searchQuery != null && getState().searchQuery == newQueryText

	fun search(queryText: String) {
		companiesRepository.logSearch(queryText)
		scope.launch(ioContext) {
			val searchResult = companiesRepository.searchCompanies(queryText, "0")
			withContext(mainContext) {
				dispatch(
					SearchResult(
						searchQuery = queryText,
						searchResult = Ok(searchResult),
					),
				)
			}
		}
	}

	private fun loadMoreSearch(getState: () -> State) {
		val state = getState()
		if (state.searchResults.size < state.totalResults) {
			scope.launch(ioContext) {
				val searchQuery = state.searchQuery
				val searchResult = companiesRepository.searchCompanies(
					searchQuery!!,
					(state.searchResults.size).toString()
				)
				withContext(mainContext) {
					dispatch(MoreSearchResult(searchResult = Ok(searchResult)))
				}
			}
		}
	}

	private fun searchItemClicked(name: String, number: String) {

		val newSearchHistoryItem = SearchHistoryItem(
			name,
			number,
			System.currentTimeMillis()
		)

		scope.launch(ioContext) {
			val searchHistoryItems = companiesRepository.addRecentSearchItem(newSearchHistoryItem)
			val recentSearchesWithSearchTime = updateHistoryWithSearchTime(searchHistoryItems)
			withContext(mainContext) {
				dispatch(Message.SearchItemClicked(recentSearchesWithSearchTime))
			}
		}

	}

	//endregion

}
