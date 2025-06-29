package com.babestudios.companyinfouk.shared.screen.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.shared.domain.model.common.ApiResult
import com.babestudios.companyinfouk.shared.screen.main.MainStore.Intent
import com.babestudios.companyinfouk.shared.screen.main.MainStore.SideEffect
import com.babestudios.companyinfouk.shared.screen.main.MainStore.State
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.shared.domain.model.search.FilterState
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.shared.domain.model.search.filterSearchResults
import com.github.michaelbull.result.fold

class MainStoreFactory(
	private val storeFactory: StoreFactory,
	private val mainExecutor: MainExecutor,
) {

	fun create(): MainStore =
		object : MainStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "MainStore",
			initialState = State(),
			bootstrapper = MainBootstrapper(),
			executorFactory = { mainExecutor },
			reducer = MainReducer
		) {}

	private class MainBootstrapper : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.ShowRecentSearches)
		}
	}

	private object MainReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.ShowRecentSearches -> copy(
					isLoading = false,
					searchHistoryItems = msg.searchHistoryItems,
					searchQuery = "",
					searchResults = emptyList(),
					filteredSearchResultItems = emptyList(),
					filterState = FilterState.FILTER_SHOW_ALL,
				)
				is Message.SearchResult -> msg.searchResult.fold(
					success = { copy(
						isLoading = false,
						searchResults = it.items,
						totalResults = it.totalResults,
						filteredSearchResultItems = it.items.filterSearchResults(filterState)
					) },
					failure = { copy(isLoading = false, error = it) }
				)

				is Message.MoreSearchResult -> msg.searchResult.fold(
					success = {
						copy(
							isLoading = false,
							searchResults = searchResults.plus(it.items),
							totalResults = it.totalResults,
						)
					},
					failure = { copy(isLoading = false, error = it) }
				)
				is Message.SetFilterState -> {
					val result = if (msg.filterState.ordinal > FilterState.FILTER_SHOW_ALL.ordinal) {
						this.searchResults.filterSearchResults(msg.filterState)
					} else {
						this.searchResults
					}
					copy(
						filteredSearchResultItems = result,
						filterState = msg.filterState
					)
				}
				is Message.SearchItemClicked -> copy(searchHistoryItems = msg.searchHistoryItems)
				is Message.SetSearchMenuItemExpanded -> copy(searchQuery = "")
				is Message.SetSearchMenuItemCollapsed -> {
					copy(
						searchQuery = "",
						searchResults = emptyList(),
						filteredSearchResultItems = emptyList(),
						filterState = FilterState.FILTER_SHOW_ALL,
					)
				}
			}
	}

}

sealed class Message {
	data class ShowRecentSearches(
		val searchHistoryItems: List<SearchHistoryItem>,
	) : Message()

	data class SearchResult(val searchQuery: String?, val searchResult: ApiResult<CompanySearchResult>) : Message()
	data class MoreSearchResult(val searchResult: ApiResult<CompanySearchResult>) : Message()
	class SetFilterState(val filterState: FilterState) : Message()
	class SearchItemClicked(val searchHistoryItems: List<SearchHistoryItem>) : Message()
	data object SetSearchMenuItemExpanded : Message()
	data object SetSearchMenuItemCollapsed : Message()
}

sealed class BootstrapIntent {
	data object ShowRecentSearches : BootstrapIntent()
}
