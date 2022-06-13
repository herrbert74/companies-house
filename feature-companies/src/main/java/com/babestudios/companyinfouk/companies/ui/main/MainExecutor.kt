package com.babestudios.companyinfouk.companies.ui.main

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.companies.ui.main.MainStore.Intent
import com.babestudios.companyinfouk.companies.ui.main.MainStore.State
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryHeaderItem
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryHeaderVisitable
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryVisitable
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryVisitableBase
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.utils.StringResourceHelper
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.search.FilterState
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.domain.model.search.filterSearchResults
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	private val stringResourceHelper: StringResourceHelper,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.ShowRecentSearches -> {
				companiesRepository.logScreenView("Main")
				showRecentSearches()
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			Intent.ShowRecentSearches -> showRecentSearches()
			Intent.ClearRecentSearchesClicked -> {
				if (getState().state is MainStore.StateMachine.ShowRecentSearches &&
					(getState().state as MainStore.StateMachine.ShowRecentSearches).searchHistoryVisitables.isNotEmpty()
				)
					publish(SideEffect.ShowDeleteRecentSearchesDialog)
			}
			Intent.ClearRecentSearches -> clearAllRecentSearches()
			Intent.ClearSearch -> clearSearch()
			is Intent.LoadMoreSearch -> loadMoreSearch(intent.page, getState)
			is Intent.SearchHistoryItemClicked -> publish(SideEffect.SearchItemClicked(intent.searchHistoryItem))
			is Intent.SearchItemClicked -> searchItemClicked(intent.name, intent.number)
			is Intent.SearchQueryChanged -> onSearchQueryChanged(intent.queryText, getState)
			is Intent.SetFilterState -> dispatch(Message.SetFilterState(intent.filterState))
			is Intent.SetSearchMenuItemExpanded -> dispatch(Message.SetSearchMenuItemExpanded(intent.expanded))
		}
	}

	//region Recent searches

	private fun showRecentSearches() {
		scope.launch(ioContext) {
			val recentSearches = companiesRepository.recentSearches()
			val recentSearchVisitables = convertSearchHistoryToVisitables(recentSearches)
			withContext(mainContext) {
				dispatch(Message.ShowRecentSearches(recentSearchVisitables))
			}
		}
	}

	private fun convertSearchHistoryToVisitables(reply: List<SearchHistoryItem>): List<SearchHistoryVisitableBase> {
		val searchHistoryVisitables: MutableList<SearchHistoryVisitableBase> =
			reply.map { item ->
				SearchHistoryVisitable(
					SearchHistoryItem(
						item.companyName,
						item.companyNumber,
						System.currentTimeMillis()
					)
				)
			}.toMutableList()
		if (searchHistoryVisitables.size > 0)
			searchHistoryVisitables.add(
				0,
				SearchHistoryHeaderVisitable(SearchHistoryHeaderItem(stringResourceHelper.getRecentSearchesString()))
			)
		return searchHistoryVisitables
	}

	private fun clearAllRecentSearches() {
		scope.launch(ioContext) {
			companiesRepository.clearAllRecentSearches()
			withContext(mainContext) {
				dispatch(Message.ShowRecentSearches(convertSearchHistoryToVisitables(emptyList())))
			}
		}

	}

	//endregion

	//region Search

	//TODO
//	fun setSearchMenuItemExpanded(expanded: Boolean) {
//		setState {
//			copy(
//				isSearchMenuItemExpanded = expanded
//			)
//		}
//	}

	private fun onSearchQueryChanged(queryText: String, getState: () -> State) {
		when {
			isComingBackFromCompanyScreen(queryText, getState) -> return
			queryText.length > 2 -> search(queryText, getState)
			else -> {
				dispatch(
					Message.ShowSearchResults(
						timeStamp = System.currentTimeMillis(),
						searchQuery = queryText,
						totalCount = 0,
						searchResultItems = emptyList(),
						filteredSearchResultItems = emptyList()
					),
				)
			}
		}
	}

	private fun isComingBackFromCompanyScreen(newQueryText: String, getState: () -> State): Boolean {
		return getState().state is MainStore.StateMachine.ShowSearchResults &&
			(getState().state as MainStore.StateMachine.ShowSearchResults).searchQuery.isNotEmpty() &&
			(getState().state as MainStore.StateMachine.ShowSearchResults).searchQuery == newQueryText
	}

	fun search(queryText: String, getState: () -> State) {
		logSearch(queryText)
		scope.launch(ioContext) {
			val searchResult = companiesRepository.searchCompanies(queryText, "0")
			val filterState = (getState().state  as? MainStore.StateMachine.ShowSearchResults)?.filterState
				?: FilterState.FILTER_SHOW_ALL

			withContext(mainContext) {
				dispatch(
					Message.ShowSearchResults(
						timeStamp = System.currentTimeMillis(),
						searchQuery = queryText,
						totalCount = searchResult.totalResults ?: 0,
						searchResultItems = searchResult.items,
						filteredSearchResultItems = searchResult.items.filterSearchResults(filterState)
					),
				)
			}
		}
	}

	private fun loadMoreSearch(page: Int, getState: () -> State) {
		val showState = getState().state as MainStore.StateMachine.ShowSearchResults
		scope.launch(ioContext) {
			val searchResult = companiesRepository.searchCompanies(
				showState.searchQuery,
				(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString()
			)
			val filterState = showState.filterState
			val newItems = showState.searchResultItems.toMutableList()
			newItems.addAll(searchResult.items)
			val filteredSearchResultItems = newItems.filterSearchResults(filterState)
			withContext(mainContext) {
				dispatch(
					Message.ShowSearchResults(
						timeStamp = System.currentTimeMillis(),
						searchQuery = showState.searchQuery,
						totalCount = searchResult.totalResults ?: 0,
						searchResultItems = newItems.toList(),
						filteredSearchResultItems = filteredSearchResultItems
					),
				)
			}
		}
	}

	private fun logSearch(queryText: String) {
		companiesRepository.logSearch(queryText)
	}

	//endregion

	//region main actions

	private fun searchItemClicked(name: String, number: String) {
		//TODO Simply pass the adapter position, so it can be tested?
		val newSearchHistoryItem = SearchHistoryItem(
			name,
			number,
			System.currentTimeMillis()
		)
		//val searchHistoryItems =
			companiesRepository.addRecentSearchItem(
			newSearchHistoryItem
		)
		//TODO Maybe we need to add history items to search state?
//		setState {
//			copy(
//				companyName = name,
//				companyNumber = number,
//				searchHistoryVisitables = convertSearchHistoryToVisitables(searchHistoryItems)
//			)
//		}
		publish(SideEffect.SearchItemClicked(newSearchHistoryItem))
	}

	private fun clearSearch() {
		dispatch(
			Message.ShowSearchResults(
				timeStamp = System.currentTimeMillis(),
				searchQuery = "",
				totalCount = 0,
				searchResultItems = emptyList(),
				filteredSearchResultItems = emptyList(),
			),
		)
	}

	//endregion

}
