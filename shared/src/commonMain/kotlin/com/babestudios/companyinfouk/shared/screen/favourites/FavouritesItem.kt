package com.babestudios.companyinfouk.shared.screen.favourites

import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem

data class FavouritesItem(
	val searchHistoryItem: SearchHistoryItem,
	var isPendingRemoval: Boolean = false,
)
