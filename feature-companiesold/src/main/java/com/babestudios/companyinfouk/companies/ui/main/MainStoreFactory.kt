package com.babestudios.companyinfouk.companies.ui.main

import androidx.lifecycle.SavedStateHandle
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.companies.ui.main.MainStore.Intent
import com.babestudios.companyinfouk.companies.ui.main.MainStore.State
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryVisitableBase
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResultItem
import com.babestudios.companyinfouk.domain.model.search.FilterState
import com.babestudios.companyinfouk.domain.model.search.filterSearchResults

class MainStoreFactory(
	private val storeFactory: StoreFactory,
	private val mainExecutor: MainExecutor,
	private val savedStateHandle: SavedStateHandle
) {

	fun createOrRetrieve(): MainStore = create(savedStateHandle.get(MAIN_STATE))

	fun create(initialState: State? = null): MainStore =
		object : MainStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "MainStore",
			initialState = initialState ?: State(),
			bootstrapper = MainBootstrapper(),
			executorFactory = { mainExecutor },
			reducer = MainReducer(savedStateHandle)
		) {}

	private class MainBootstrapper : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.ShowRecentSearches)
		}
	}

	private class MainReducer(val savedStateHandle: SavedStateHandle) : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State {
			return when (msg) {
				is Message.ShowRecentSearches -> copy(
					isLoading = false,
					searchHistoryVisitables = msg.searchHistoryVisitables,
					searchQuery = null,
					searchResultItems = emptyList(),
					filteredSearchResultItems = emptyList(),
					filterState = FilterState.FILTER_SHOW_ALL,
				)
				is Message.ShowSearchResults -> copy(
					isLoading = false,
					searchQuery = msg.searchQuery,
					searchResultItems = msg.searchResultItems,
					filteredSearchResultItems = msg.filteredSearchResultItems,
					filterState = FilterState.FILTER_SHOW_ALL,
				)
				is Message.SetFilterState -> {
					val result = if (msg.filterState.ordinal > FilterState.FILTER_SHOW_ALL.ordinal) {
						this.searchResultItems.filterSearchResults(msg.filterState)
					} else {
						this.searchResultItems
					}
					copy(
						filteredSearchResultItems = result,
						filterState = msg.filterState
					)
				}
				is Message.SearchItemClicked -> {
					copy(searchHistoryVisitables = msg.searchHistoryVisitables)
				}
				is Message.SetSearchMenuItemExpanded ->
					copy(
						searchQuery = "",
					)
				is Message.SetSearchMenuItemCollapsed -> {
					copy(
						searchQuery = null,
						searchResultItems = emptyList(),
						filteredSearchResultItems = emptyList(),
						filterState = FilterState.FILTER_SHOW_ALL,
					)
				}
			}.also { newState ->
				savedStateHandle.set(MAIN_STATE, newState)
			}
		}
	}
}

sealed interface BootstrapIntent {
	object ShowRecentSearches : BootstrapIntent
}

sealed class Message {

	data class ShowRecentSearches(
		val searchHistoryVisitables: List<SearchHistoryVisitableBase>,
	) : Message()

	data class ShowSearchResults(
		val timeStamp: Long = 0L,
		val totalCount: Int,
		val searchQuery: String,
		val searchResultItems: List<CompanySearchResultItem>,
		val filteredSearchResultItems: List<CompanySearchResultItem>,
	) : Message()

	class SetFilterState(val filterState: FilterState) : Message()
	class SearchItemClicked(val searchHistoryVisitables: List<SearchHistoryVisitableBase>) : Message()
	object SetSearchMenuItemExpanded : Message()
	object SetSearchMenuItemCollapsed : Message()

}
