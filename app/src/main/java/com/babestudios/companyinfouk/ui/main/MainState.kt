package com.babestudios.companyinfouk.ui.main

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.ui.main.recents.AbstractSearchHistoryVisitable
import com.babestudios.companyinfouk.ui.main.search.AbstractSearchVisitable
import kotlinx.android.parcel.Parcelize

enum class ContentChange {
	NONE,
	SEARCH_ITEMS_RECEIVED,
	SEARCH_ITEMS_RECEIVED_FROM_SAVED_INSTANCE_STATE,
	SEARCH_HISTORY_ITEMS_RECEIVED,
	SEARCH_HISTORY_ITEMS_UPDATED,

	//Transient states
	DELETE_SEARCH_HISTORY
}

@Parcelize
data class MainState(
		var searchVisitables: List<AbstractSearchVisitable> = ArrayList(),
		var searchHistoryVisitables: List<AbstractSearchHistoryVisitable>?,
		var totalCount: Int? = null,
		var queryText: String = "",
		var isSearchLoading: Boolean = false,
		var contentChange: ContentChange = ContentChange.NONE,
		var filterState: FilterState = FilterState.FILTER_SHOW_ALL,
		var filteredSearchVisitables: List<AbstractSearchVisitable> = ArrayList()
) : BaseState(), Parcelable

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