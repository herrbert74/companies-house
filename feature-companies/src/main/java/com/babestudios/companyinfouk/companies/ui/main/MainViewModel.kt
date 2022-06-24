package com.babestudios.companyinfouk.companies.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.events
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.companies.ui.main.MainStore.Intent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map

const val MAIN_STATE = "MainState"

@HiltViewModel
class MainViewModel @Inject constructor(
	mainExecutor: MainExecutor,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {

	private var mainStore: MainStore

	init {
		mainStore = MainStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), mainExecutor, savedStateHandle)
			.createOrRetrieve()
	}

	fun onViewCreated(
		view: MainFragment,
		lifecycle: Lifecycle
	) {
		bind(lifecycle, BinderLifecycleMode.RESUME_PAUSE) {
			mainStore.states bindTo view
			mainStore.labels bindTo { view.sideEffects(it) }
			view.events.map { userIntentToIntent(it) } bindTo mainStore
		}
	}

	private val userIntentToIntent: UserIntent.() -> Intent =
		{
			when (this) {
				UserIntent.ClearRecentSearchesClicked -> Intent.ClearRecentSearchesClicked
				UserIntent.ClearRecentSearches -> Intent.ClearRecentSearches
				is UserIntent.LoadMoreSearch -> Intent.LoadMoreSearch(page)
				is UserIntent.SearchHistoryItemClicked -> Intent.SearchHistoryItemClicked(searchHistoryItem)
				is UserIntent.SearchItemClicked -> Intent.SearchItemClicked(name, number)
				is UserIntent.SearchQueryChanged -> Intent.SearchQueryChanged(queryText)
				is UserIntent.SetFilterState -> Intent.SetFilterState(filterState)
				UserIntent.ShowRecentSearches -> Intent.ShowRecentSearches
				is UserIntent.SetSearchMenuItemExpanded -> Intent.SetSearchMenuItemExpanded
				is UserIntent.SetSearchMenuItemCollapsed -> Intent.SetSearchMenuItemCollapsed
			}
		}

	override fun onCleared() {
		mainStore::dispose
	}

}
