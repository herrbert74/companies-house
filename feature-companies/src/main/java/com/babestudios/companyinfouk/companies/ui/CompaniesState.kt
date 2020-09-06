package com.babestudios.companyinfouk.companies.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.common.model.company.Company
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesVisitable
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryVisitableBase
import com.babestudios.companyinfouk.companies.ui.main.search.SearchVisitableBase
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem

data class CompaniesState(

		//Needed to trigger filtered search result changes when only the state changes, but not the result
		val timeStamp: Long = 0L,

		//Main/Search
		val searchRequest: Async<CompanySearchResult> = Uninitialized,
		val searchVisitables: List<SearchVisitableBase> = emptyList(),
		val filteredSearchVisitables: List<SearchVisitableBase> = ArrayList(),
		val totalCount: Int = 0,
		@PersistState
		val queryText: String = "",
		val isSearchLoading: Boolean = false,
		val isSearchMenuItemExpanded: Boolean = false,
		@PersistState
		val filterState: FilterState = FilterState.FILTER_SHOW_ALL,

		//Main/Search history
		val searchHistoryRequest: Async<List<SearchHistoryItem>> = Uninitialized,
		val searchHistoryVisitables: List<SearchHistoryVisitableBase> = emptyList(),

		//Company
		val companyRequest: Async<Company> = Uninitialized,
		@PersistState
		var company: Company = Company(),
		@PersistState
		val companyNumber: String = "",
		@PersistState
		val companyName: String = "",
		val isFavorite: Boolean = false,

		//Favourites
		val favouritesRequest: Async<List<SearchHistoryItem>> = Uninitialized,
		val favouriteItems: List<FavouritesVisitable> = emptyList(),

) : MvRxState

enum class FilterState(private val name2: String) {
	FILTER_SHOW_ALL("all"),
	FILTER_ACTIVE("active"),
	FILTER_LIQUIDATION("liquidation"),
	FILTER_OPEN("open"),
	FILTER_DISSOLVED("dissolved");

	override fun toString(): String {
		return name2
	}
}
