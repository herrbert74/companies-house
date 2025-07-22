package com.babestudios.companyinfouk.shared.screen.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResultItem
import com.babestudios.companyinfouk.shared.domain.model.search.FilterState
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.shared.ext.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
interface MainComp {

	fun onItemClicked(companySearchResultItem: CompanySearchResultItem)
	fun onRecentSearchesItemClicked(searchHistoryItem: SearchHistoryItem)
	fun onClearRecentSearchesClicked()

	fun onSearchQueryChanged(searchQuery: String?)

	fun showRecentSearches()
	fun clearRecentSearchesClicked()
	fun clearRecentSearches()

	fun loadMoreSearch()

	fun setFilterState(filterState: FilterState)
	fun setSearchMenuItemExpanded()
	fun setSearchMenuItemCollapsed()

	fun onBackClicked()
	fun onFavoritesClicked()
	fun onPrivacyClicked()

	val state: Value<MainStore.State>

	sealed class Output {
		data class Selected(val companySearchResultItem: CompanySearchResultItem) : Output()
		data object Privacy : Output()
		data class RecentSearchHistorySelected(val searchHistoryItem: SearchHistoryItem) : Output()
		data object Favourites : Output()
	}

}

@Suppress("TooManyFunctions")
class MainComponent(
	componentContext: ComponentContext,
	val mainExecutor: MainExecutor,
	private val output: FlowCollector<MainComp.Output>,
	private val finishHandler: () -> Unit,
) : MainComp, ComponentContext by componentContext {

	private var mainStore: MainStore =
		MainStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), mainExecutor).create()

	override fun onItemClicked(companySearchResultItem: CompanySearchResultItem) {
		CoroutineScope(mainExecutor.mainContext).launch {
			mainStore.accept(
				MainStore.Intent.SearchItemClicked(
					companySearchResultItem.title.orEmpty(),
					companySearchResultItem.companyNumber.orEmpty()
				)
			)
			output.emit(MainComp.Output.Selected(companySearchResultItem))
		}
	}

	override fun onRecentSearchesItemClicked(searchHistoryItem: SearchHistoryItem) {
		CoroutineScope(mainExecutor.mainContext).launch {
			output.emit(MainComp.Output.RecentSearchHistorySelected(searchHistoryItem))
		}
	}

	override fun onClearRecentSearchesClicked() {
		mainStore.accept(MainStore.Intent.ClearRecentSearches)
	}

	override fun onSearchQueryChanged(searchQuery: String?) {
		mainStore.accept(MainStore.Intent.SearchQueryChanged(searchQuery))
	}

	override fun showRecentSearches() {
		mainStore.accept(MainStore.Intent.ShowRecentSearches)
	}

	override fun clearRecentSearchesClicked() {
		mainStore.accept(MainStore.Intent.ClearRecentSearchesClicked)
	}

	override fun clearRecentSearches() {
		mainStore.accept(MainStore.Intent.ClearRecentSearches)
	}

	override fun loadMoreSearch() {
		mainStore.accept(MainStore.Intent.LoadMoreSearch)
	}

	override fun setFilterState(filterState: FilterState) {
		mainStore.accept(MainStore.Intent.SetFilterState(filterState))
	}

	override fun setSearchMenuItemExpanded() {
		mainStore.accept(MainStore.Intent.SetSearchMenuItemExpanded)
	}

	override fun setSearchMenuItemCollapsed() {
		mainStore.accept(MainStore.Intent.SetSearchMenuItemCollapsed)
	}

	override fun onFavoritesClicked() {
		CoroutineScope(mainExecutor.mainContext).launch {
			output.emit(MainComp.Output.Favourites)
		}
	}

	override fun onPrivacyClicked() {
		CoroutineScope(mainExecutor.mainContext).launch {
			output.emit(MainComp.Output.Privacy)
		}
	}

	override fun onBackClicked() {
		finishHandler.invoke()
		mainStore.dispose()
	}

	override val state: Value<MainStore.State>
		get() = mainStore.asValue()

}
