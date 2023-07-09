package com.babestudios.companyinfouk.data.local

import android.content.SharedPreferences
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.lighthousegames.logging.logging

const val PREF_FILE_NAME = "companies_house_pref_file"
const val PREF_FAVOURITES = "companies_house_favourites"
const val PREF_LATEST_SEARCHES = "companies_house_latest_searches"
const val PREF_LATEST_SEARCHES_SIZE = 10

class PreferencesHelper internal constructor(private val sharedPreferences: SharedPreferences, private val json: Json) {

	private val log = logging()

	val recentSearches: List<SearchHistoryItem>
		get() {
			val latestSearches = sharedPreferences.getString(PREF_LATEST_SEARCHES, "")
			var searchItems: Array<SearchHistoryItem>? = null
			if (latestSearches?.isEmpty() == false) {
				try {
					searchItems = json.decodeFromString<Array<SearchHistoryItem>>(latestSearches)
				} catch (e: SerializationException) {
					log.d { "getRecentSearches error: " + e.localizedMessage }
				}
			}
			return searchItems.orEmpty().toList()
		}

	val favourites: Array<SearchHistoryItem>
		get() {
			val favourites = sharedPreferences.getString(PREF_FAVOURITES, "")
			var favouritesArray: Array<SearchHistoryItem> = arrayOf()
			if (favourites?.isEmpty() == false) {
				try {
					favouritesArray = json.decodeFromString(favourites)
				} catch (e: SerializationException) {
					log.d { "getFavourites error: " + e.localizedMessage }
				}
			}
			return favouritesArray
		}

	fun addRecentSearch(searchItem: SearchHistoryItem): ArrayList<SearchHistoryItem> {
		val latestSearches = ArrayList(recentSearches)
		if (latestSearches.contains(searchItem)) {
			latestSearches.remove(searchItem)
		}
		latestSearches.add(0, searchItem)
		if (latestSearches.size > PREF_LATEST_SEARCHES_SIZE) {
			latestSearches.removeAt(latestSearches.lastIndex)
		}
		val latestSearchesString = json.encodeToString(latestSearches)
		sharedPreferences.edit().putString(PREF_LATEST_SEARCHES, latestSearchesString).apply()
		return ArrayList(latestSearches)
	}

	fun clearAllRecentSearches() {
		sharedPreferences.edit().putString(PREF_LATEST_SEARCHES, "").apply()
	}

	fun addFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		val favouritesList = favourites.toMutableList()
		return if (favouritesList.contains(searchHistoryItem)) {
			false
		} else {
			favouritesList.add(searchHistoryItem)
			val favouritesString = json.encodeToString(favouritesList.toTypedArray())
			sharedPreferences.edit().putString(PREF_FAVOURITES, favouritesString).apply()
			true
		}
	}

	fun clearAllFavourites() {
		sharedPreferences.edit().putString(PREF_FAVOURITES, "").apply()
	}

	fun removeFavourite(favouriteToDelete: SearchHistoryItem) {
		val favouritesList = favourites.toMutableList()
		if (favouritesList.contains(favouriteToDelete)) {
			favouritesList.remove(favouriteToDelete)
		}
		val favouritesString = json.encodeToString(favouritesList.toTypedArray())
		sharedPreferences.edit().putString(PREF_FAVOURITES, favouritesString).apply()
	}

}
