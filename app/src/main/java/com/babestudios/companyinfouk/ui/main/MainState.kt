package com.babestudios.companyinfouk.ui.main

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.ui.main.recents.AbstractSearchHistoryVisitable
import com.babestudios.companyinfouk.ui.main.search.AbstractSearchVisitable
import kotlinx.android.parcel.Parcelize

enum class ContentChange {
	NONE,
	SEARCH_ITEMS_RECEIVED,
	SEARCH_HISTORY_ITEMS_RECEIVED,
	SEARCH_HISTORY_ITEMS_UPDATED,

	//Transient states
	DELETE_SEARCH_HISTORY
}

@Parcelize
data class SearchState(
		var searchItems: List<AbstractSearchVisitable>?,
		var searchHistoryItems: List<AbstractSearchHistoryVisitable>?,
		var totalCount: Int? = null,
		var queryText: String = "",
		var isSearchLoading: Boolean = false,
		var contentChange: ContentChange = ContentChange.NONE
) : BaseState(), Parcelable