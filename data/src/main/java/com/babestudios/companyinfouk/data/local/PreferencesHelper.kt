package com.babestudios.companyinfouk.data.local

import android.content.SharedPreferences
import android.util.Log
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.google.gson.Gson
import com.google.gson.JsonParseException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

const val PREF_FILE_NAME = "companies_house_pref_file"
const val PREF_FAVOURITES = "companies_house_favourites"
const val PREF_LATEST_SEARCHES = "companies_house_latest_searches"
const val PREF_LATEST_SEARCHES_SIZE = 10

@Singleton
class PreferencesHelper @Inject
internal constructor(private val sharedPreferences: SharedPreferences, private val gson: Gson) {

	val recentSearches: List<SearchHistoryItem>
		get() {
			val latestSearches = sharedPreferences.getString(PREF_LATEST_SEARCHES, "")
			var searchItems: Array<SearchHistoryItem>? = null
			if (latestSearches?.isEmpty() == false) {
				try {
					searchItems = gson.fromJson(latestSearches, Array<SearchHistoryItem>::class.java)
				} catch (e: JsonParseException) {
					Log.d("test", "getRecentSearches error: " + e.localizedMessage)
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
					favouritesArray = gson.fromJson(favourites, Array<SearchHistoryItem>::class.java)
				} catch (e: JsonParseException) {
					Log.d("test", "getFavourites error: " + e.localizedMessage)
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
		val latestSearchesString = gson.toJson(latestSearches)
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
			val favouritesString = gson.toJson(favouritesList.toTypedArray())
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
		val favouritesString = gson.toJson(favouritesList.toTypedArray())
		sharedPreferences.edit().putString(PREF_FAVOURITES, favouritesString).apply()
	}

}
