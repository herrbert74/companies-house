package com.babestudios.companyinfouk.shared.data.local

import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem

interface Prefs {
	val recentSearches: List<SearchHistoryItem>
	val favourites: Array<SearchHistoryItem>

	fun addRecentSearch(searchItem: SearchHistoryItem): ArrayList<SearchHistoryItem>
	fun clearAllRecentSearches()
	fun addFavourite(searchHistoryItem: SearchHistoryItem): Boolean
	fun clearAllFavourites()
	fun removeFavourite(favouriteToDelete: SearchHistoryItem)
}
