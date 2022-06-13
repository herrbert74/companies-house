package com.babestudios.companyinfouk.companies.ui.main

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.search.FilterState
import com.babestudios.companyinfouk.companies.ui.main.MainStore.Intent
import com.babestudios.companyinfouk.companies.ui.main.MainStore.State
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryVisitableBase
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResultItem
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem

interface MainStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		object ShowRecentSearches : Intent()
		object ClearRecentSearchesClicked : Intent()
		object ClearRecentSearches : Intent()
		data class SearchHistoryItemClicked(val searchHistoryItem: SearchHistoryItem) : Intent()
		data class SearchQueryChanged(val queryText: String) : Intent()
		data class LoadMoreSearch(val page: Int) : Intent()
		data class SearchItemClicked(val name: String, val number: String) : Intent()
		object ClearSearch : Intent()
		data class SetFilterState(val filterState: FilterState) : Intent()
		data class SetSearchMenuItemExpanded(val expanded: Boolean) : Intent()
	}

	data class State(
		val state: StateMachine,
		val isSearchMenuItemExpanded: Boolean = false,
	)

	sealed class StateMachine {
		object Loading : StateMachine()

		class ShowRecentSearches(
			val searchHistoryVisitables: List<SearchHistoryVisitableBase>,
		) : StateMachine()

		data class ShowSearchResults(
			val timeStamp: Long = 0L,
			val searchQuery: String  = "",
			val searchResultItems: List<CompanySearchResultItem> = emptyList(),
			val filteredSearchResultItems: List<CompanySearchResultItem> = emptyList(),
			val filterState: FilterState = FilterState.FILTER_SHOW_ALL,
		) : StateMachine()

		class SearchError(val t: Throwable) : StateMachine()
		class SearchHistoryError(val t: Throwable) : StateMachine()
	}

}
