package com.babestudios.companyinfouk.shared.screen.main

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.shared.screen.main.MainStore.Intent
import com.babestudios.companyinfouk.shared.screen.main.MainStore.SideEffect
import com.babestudios.companyinfouk.shared.screen.main.MainStore.State
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResultItem
import com.babestudios.companyinfouk.shared.domain.model.search.FilterState
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem

interface MainStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data object ShowRecentSearches : Intent()
		data object ClearRecentSearchesClicked : Intent()
		data object ClearRecentSearches : Intent()

		data class SearchQueryChanged(val queryText: String?) : Intent()
		data object LoadMoreSearch : Intent()
		data class SearchItemClicked(val name: String, val number: String) : Intent()

		data class SetFilterState(val filterState: FilterState) : Intent()
		data object SetSearchMenuItemExpanded : Intent()
		data object SetSearchMenuItemCollapsed : Intent()
	}

	data class State(

		//recent search history data
		val searchHistoryItems: List<SearchHistoryItem> = emptyList(),

		//search result data
		var totalResults: Int = 0,
		var searchResults: List<CompanySearchResultItem> = emptyList(),
		val filteredSearchResultItems: List<CompanySearchResultItem> = emptyList(),

		val error: Throwable? = null,

		//state
		val isLoading: Boolean = true,
		val searchQuery: String? = null,
		val filterState: FilterState = FilterState.FILTER_SHOW_ALL,

		)

	sealed class SideEffect {
		data object ShowDeleteRecentSearchesDialog : SideEffect()
	}

}
