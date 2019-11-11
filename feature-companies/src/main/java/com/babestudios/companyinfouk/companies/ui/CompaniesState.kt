package com.babestudios.companyinfouk.companies.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesVisitable
import com.babestudios.companyinfouk.companies.ui.main.recents.AbstractSearchHistoryVisitable
import com.babestudios.companyinfouk.companies.ui.main.search.AbstractSearchVisitable
import com.babestudios.companyinfouk.data.model.company.Company
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem

data class CompaniesState(
		//Main/Search
		val searchRequest: Async<CompanySearchResult> = Uninitialized,
		val searchVisitables: List<AbstractSearchVisitable> = emptyList(),
		val filteredSearchVisitables: List<AbstractSearchVisitable> = ArrayList(),
		val totalCount: Int = 0,
		val queryText: String = "",
		val isSearchLoading: Boolean = false,
		val filterState: FilterState = FilterState.FILTER_SHOW_ALL,

		//Main/Search history
		val searchHistoryRequest: Async<List<SearchHistoryItem>> = Uninitialized,
		val searchHistoryVisitables: List<AbstractSearchHistoryVisitable> = emptyList(),

		//Company
		val companyRequest: Async<Company> = Uninitialized,
		var company: Company = Company(),
		val companyNumber: String = "",
		val companyName: String = "",
		val natureOfBusinessString: String = "",
		val isFavorite: Boolean = false,

		//Favourites
		val favouritesRequest: Async<List<SearchHistoryItem>> = Uninitialized,
		val favouriteItems: List<FavouritesVisitable> = emptyList(),
		val addressString: String = ""

		//Map

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