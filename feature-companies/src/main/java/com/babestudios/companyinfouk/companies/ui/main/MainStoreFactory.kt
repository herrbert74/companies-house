package com.babestudios.companyinfouk.companies.ui.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.domain.model.search.FilterState
import com.babestudios.companyinfouk.companies.ui.main.MainStore.Intent
import com.babestudios.companyinfouk.companies.ui.main.MainStore.State
import com.babestudios.companyinfouk.companies.ui.main.MainStore.StateMachine.ShowRecentSearches
import com.babestudios.companyinfouk.companies.ui.main.MainStore.StateMachine.ShowSearchResults
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryVisitableBase
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResultItem
import com.babestudios.companyinfouk.domain.model.search.filterSearchResults

class MainStoreFactory(
	private val storeFactory: StoreFactory,
	private val MainExecutor: MainExecutor
) {

	fun create(): MainStore =
		object : MainStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "MainStore",
			initialState = State(MainStore.StateMachine.Loading),
			bootstrapper = MainBootstrapper(),
			executorFactory = { MainExecutor },
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
				is Message.ShowRecentSearches -> copy(state = ShowRecentSearches(msg.searchVisitables))
				is Message.ShowSearchResults -> copy(
					state = ShowSearchResults(
						msg.timeStamp,
						msg.searchQuery,
						msg.searchResultItems,
						msg.filteredSearchResultItems,
						(this.state as? ShowSearchResults)?.filterState ?: FilterState.FILTER_SHOW_ALL,
					)
				)
				is Message.SetFilterState -> {
					val showState = this.state as? ShowSearchResults
					if (showState == null) {
						copy(state = ShowSearchResults(filterState = msg.filterState))
					} else {
						if (msg.filterState.ordinal > FilterState.FILTER_SHOW_ALL.ordinal) {
							val result = showState.searchResultItems.filterSearchResults(msg.filterState)
							copy(
								state = showState.copy(
									filteredSearchResultItems = result,
									filterState = msg.filterState
								)
							)
						} else {
							val result = showState.searchResultItems
							copy(
								state = showState.copy(
									filteredSearchResultItems = result,
									filterState = msg.filterState
								)
							)
						}
					}
				}
				is Message.SetSearchMenuItemExpanded -> copy(isSearchMenuItemExpanded = msg.expanded)
			}
	}
}

sealed interface BootstrapIntent {
	object ShowRecentSearches : BootstrapIntent
}

sealed class Message {

	data class ShowRecentSearches(
		val searchVisitables: List<SearchHistoryVisitableBase>,
	) : Message()

	data class ShowSearchResults(
		val timeStamp: Long,
		val totalCount: Int,
		val searchQuery: String,
		val searchResultItems: List<CompanySearchResultItem>,
		val filteredSearchResultItems: List<CompanySearchResultItem>,
	) : Message()

	class SetFilterState(val filterState: FilterState) : Message()
	class SetSearchMenuItemExpanded(val expanded: Boolean) : Message()

}
