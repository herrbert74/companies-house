package com.babestudios.companyinfo.data.local

import android.content.SharedPreferences
import android.util.Log

import com.babestudios.companyinfo.data.model.search.SearchHistoryItem
import com.google.gson.Gson

import java.util.ArrayList
import java.util.Arrays

import javax.inject.Inject
import javax.inject.Singleton

const val PREF_FILE_NAME = "companies_house_pref_file"
const val PREF_FAVOURITES = "companies_house_favourites"
const val PREF_LATEST_SEARCHES = "companies_house_latest_searches"

@Singleton
class PreferencesHelper @Inject
internal constructor(private val sharedPreferences: SharedPreferences, private val gson: Gson) {

	val recentSearches: List<SearchHistoryItem>
		get() {
			val latestSearches = sharedPreferences.getString(PREF_LATEST_SEARCHES, "")
			var searchItems: Array<SearchHistoryItem>? = null
			try {
				searchItems = gson.fromJson(latestSearches, Array<SearchHistoryItem>::class.java)
			} catch (e: Exception) {
				Log.d("test", "getRecentSearches error: " + e.localizedMessage)
			}

			return Arrays.asList(*searchItems ?: arrayOfNulls(0))
		}

	val favourites: Array<SearchHistoryItem>
		get() {
			val favourites = sharedPreferences.getString(PREF_FAVOURITES, "")
			var favouritesArray: Array<SearchHistoryItem> = arrayOf()
			try {
				favouritesArray = gson.fromJson(favourites, Array<SearchHistoryItem>::class.java)
			} catch (e: Exception) {
				Log.d("test", "getFavourites error: " + e.localizedMessage)
			}

			return favouritesArray
		}

	fun addRecentSearch(searchItem: SearchHistoryItem): ArrayList<SearchHistoryItem> {
		val latestSearches = ArrayList(recentSearches)
		if (latestSearches.contains(searchItem)) {
			latestSearches.remove(searchItem)
		}
		latestSearches.add(0, searchItem)
		if (latestSearches.size > 10) {
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
		val favouritesArray = favourites
		val favourites: ArrayList<SearchHistoryItem>
		favourites = if (favouritesArray.isNotEmpty()) {
			ArrayList(Arrays.asList(*favouritesArray))
		} else {
			ArrayList()
		}
		return if (favourites.contains(searchHistoryItem)) {
			false
		} else {
			favourites.add(searchHistoryItem)
			val favouritesString = gson.toJson(favourites.toTypedArray())
			sharedPreferences.edit().putString(PREF_FAVOURITES, favouritesString).apply()
			true
		}
	}

	fun clearAllFavourites() {
		sharedPreferences.edit().putString(PREF_FAVOURITES, "").apply()
	}

	fun removeFavourite(favouriteToDelete: SearchHistoryItem) {
		val favouritesArray = favourites
		val favourites: ArrayList<SearchHistoryItem>
		if (favouritesArray.isNotEmpty()) {
			favourites = ArrayList(Arrays.asList(*favouritesArray))
		} else {
			return
		}
		if (favourites.contains(favouriteToDelete)) {
			favourites.remove(favouriteToDelete)
		}
		val favouritesString = gson.toJson(favourites.toTypedArray())
		sharedPreferences.edit().putString(PREF_FAVOURITES, favouritesString).apply()
	}

}