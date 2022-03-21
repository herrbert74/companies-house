package com.babestudios.companyinfouk.companies.ui.favourites.list

import android.os.Parcelable
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavouritesListItem(
	val searchHistoryItem: SearchHistoryItem,
	var isPendingRemoval: Boolean = false
) : Parcelable
