package com.babestudios.companyinfouk.companies.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.companies.ui.main.FilterState
import com.babestudios.companyinfouk.companies.ui.main.recents.AbstractSearchHistoryVisitable
import com.babestudios.companyinfouk.companies.ui.main.search.AbstractSearchVisitable
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem

data class CompaniesState(
		//Main/Search
		val searchRequest: Async<CompanySearchResult> = Uninitialized,
		var searchVisitables: List<AbstractSearchVisitable> = ArrayList(),
		var filteredSearchVisitables: List<AbstractSearchVisitable> = ArrayList(),
		var totalCount: Int? = null,
		var queryText: String = "",
		var isSearchLoading: Boolean = false,
		val filterState: FilterState = FilterState.FILTER_SHOW_ALL,

		//Main/Search history
		val searchHistoryRequest: Async<List<SearchHistoryItem>> = Uninitialized,
		val searchHistoryVisitables: List<AbstractSearchHistoryVisitable> = emptyList()


		//Company

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