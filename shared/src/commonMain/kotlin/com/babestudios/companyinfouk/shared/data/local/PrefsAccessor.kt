package com.babestudios.companyinfouk.shared.data.local

import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.russhwolf.settings.Settings
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.lighthousegames.logging.logging

const val PREF_FAVOURITES = "companies_house_favourites"
const val PREF_LATEST_SEARCHES = "companies_house_latest_searches"
const val PREF_LATEST_SEARCHES_SIZE = 10

class PrefsAccessor internal constructor(private val sharedPreferences: Settings, private val json: Json) : Prefs {

	private val log = logging()

	override val recentSearches: List<SearchHistoryItem>
		get() {
			val latestSearches = sharedPreferences.getString(PREF_LATEST_SEARCHES, "")
			var searchItems: Array<SearchHistoryItem>? = null
			if (latestSearches.isNotEmpty()) {
				try {
					searchItems = json.decodeFromString<Array<SearchHistoryItem>>(latestSearches)
				} catch (e: SerializationException) {
					log.d { "getRecentSearches error: " + e.message }
				}
			}
			return searchItems.orEmpty().toList()
		}

	override val favourites: Array<SearchHistoryItem>
		get() {
			val favourites = sharedPreferences.getString(PREF_FAVOURITES, "")
			var favouritesArray: Array<SearchHistoryItem> = arrayOf()
			if (favourites.isNotEmpty()) {
				try {
					favouritesArray = json.decodeFromString(favourites)
				} catch (e: SerializationException) {
					log.d { "getFavourites error: " + e.message }
				}
			}
			return favouritesArray
		}

	override fun addRecentSearch(searchItem: SearchHistoryItem): ArrayList<SearchHistoryItem> {
		val latestSearches = ArrayList(recentSearches)
		if (latestSearches.contains(searchItem)) {
			latestSearches.remove(searchItem)
		}
		latestSearches.add(0, searchItem)
		if (latestSearches.size > PREF_LATEST_SEARCHES_SIZE) {
			latestSearches.removeAt(latestSearches.lastIndex)
		}
		val latestSearchesString = json.encodeToString(latestSearches)
		sharedPreferences.putString(PREF_LATEST_SEARCHES, latestSearchesString)
		return ArrayList(latestSearches)
	}

	override fun clearAllRecentSearches() {
		sharedPreferences.putString(PREF_LATEST_SEARCHES, "")
	}

	override fun addFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		val favouritesList = favourites.toMutableList()
		return if (favouritesList.contains(searchHistoryItem)) {
			false
		} else {
			favouritesList.add(searchHistoryItem)
			val favouritesString = json.encodeToString(favouritesList.toTypedArray())
			sharedPreferences.putString(PREF_FAVOURITES, favouritesString)
			true
		}
	}

	override fun clearAllFavourites() {
		sharedPreferences.putString(PREF_FAVOURITES, "")
	}

	override fun removeFavourite(favouriteToDelete: SearchHistoryItem) {
		val favouritesList = favourites.toMutableList()
		if (favouritesList.contains(favouriteToDelete)) {
			favouritesList.remove(favouriteToDelete)
		}
		val favouritesString = json.encodeToString(favouritesList.toTypedArray())
		sharedPreferences.putString(PREF_FAVOURITES, favouritesString)
	}

}
