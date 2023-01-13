package com.babestudios.companyinfouk.companies.ui.main

import android.os.Parcelable
import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.companies.ui.main.MainStore.Intent
import com.babestudios.companyinfouk.companies.ui.main.MainStore.State
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryVisitableBase
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResultItem
import com.babestudios.companyinfouk.domain.model.search.FilterState
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import kotlinx.parcelize.Parcelize

interface MainStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		object ShowRecentSearches : Intent()
		object ClearRecentSearchesClicked : Intent()
		object ClearRecentSearches : Intent()
		data class SearchHistoryItemClicked(val searchHistoryItem: SearchHistoryItem) : Intent()
		data class SearchQueryChanged(val queryText: String) : Intent()
		data class LoadMoreSearch(val page: Int) : Intent()
		data class SearchItemClicked(val name: String, val number: String) : Intent()
		data class SetFilterState(val filterState: FilterState) : Intent()
		object SetSearchMenuItemExpanded : Intent()
		object SetSearchMenuItemCollapsed : Intent()
	}

	@Parcelize
	data class State(
		val isLoading: Boolean = true,
		val searchHistoryVisitables: List<SearchHistoryVisitableBase> = emptyList(),
		val timeStamp: Long = 0L,
		val searchQuery: String? = null,
		val searchResultItems: List<CompanySearchResultItem> = emptyList(),
		val filteredSearchResultItems: List<CompanySearchResultItem> = emptyList(),
		val filterState: FilterState = FilterState.FILTER_SHOW_ALL,
		val error: Throwable? = null,
	) : Parcelable

}
