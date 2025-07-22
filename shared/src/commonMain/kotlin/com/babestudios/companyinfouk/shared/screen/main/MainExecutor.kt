package com.babestudios.companyinfouk.shared.screen.main

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.shared.screen.main.MainStore.Intent
import com.babestudios.companyinfouk.shared.screen.main.MainStore.SideEffect
import com.babestudios.companyinfouk.shared.screen.main.MainStore.State
import com.babestudios.companyinfouk.shared.screen.main.Message.MoreSearchResult
import com.babestudios.companyinfouk.shared.screen.main.Message.SearchResult
import com.babestudios.companyinfouk.shared.screen.main.Message.SetSearchMenuItemCollapsed
import com.babestudios.companyinfouk.shared.screen.main.Message.SetSearchMenuItemExpanded
import com.babestudios.companyinfouk.shared.screen.main.Message.ShowRecentSearches
import com.github.michaelbull.result.Ok
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainExecutor(
	private val companiesRepository: CompaniesRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent) {
		when (action) {
			is BootstrapIntent.ShowRecentSearches -> {
				companiesRepository.logScreenView("Main")
				if (state().searchQuery == null) {
					showRecentSearches()
				} else {
					state().searchQuery?.let { onSearchQueryChanged(it, state()) }
				}
			}
		}
	}

	override fun executeIntent(intent: Intent) {
		when (intent) {
			Intent.ShowRecentSearches -> showRecentSearches()

			Intent.ClearRecentSearchesClicked ->
				if (state().searchHistoryItems.isNotEmpty()) publish(SideEffect.ShowDeleteRecentSearchesDialog)

			Intent.ClearRecentSearches -> clearRecentSearches()
			is Intent.SearchQueryChanged -> onSearchQueryChanged(intent.queryText, state())
			is Intent.LoadMoreSearch -> loadMoreSearch(state())
			is Intent.SearchItemClicked -> searchItemClicked(intent.name, intent.number)
			is Intent.SetFilterState -> dispatch(Message.SetFilterState(intent.filterState))
			is Intent.SetSearchMenuItemExpanded -> if (state().searchQuery == null) dispatch(SetSearchMenuItemExpanded)
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
		return reply.map { item -> item.copy(searchTime = getTimeMillis()) }
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

	private fun onSearchQueryChanged(queryText: String?, state: State) {
		when {
			isComingBackFromCompanyScreen(queryText, state) || state.searchQuery == null -> return
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

	private fun isComingBackFromCompanyScreen(newQueryText: String?, state: State) =
		state.searchQuery != null && state.searchQuery == newQueryText

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

	private fun loadMoreSearch(state: State) {
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
			getTimeMillis()
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
