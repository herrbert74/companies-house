package com.babestudios.companieshouse.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;
import com.babestudios.companieshouse.injection.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {
	private final SharedPreferences sharedPreferences;
	private final Gson gson;

	private static final String PREF_FILE_NAME = "companies_house_pref_file";
	private static final String PREF_LATEST_SEARCHES = "companies_house_latest_searches";

	@Inject
	PreferencesHelper(@ApplicationContext Context context) {
		sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
		gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
				.create();
	}

	public ArrayList<SearchHistoryItem> addRecentSearch(SearchHistoryItem searchItem) {
		SearchHistoryItem[] latestSearches = getRecentSearches();
		ArrayList<SearchHistoryItem> latestSearchesList;
		if (latestSearches != null) {
			latestSearchesList = new ArrayList<>(Arrays.asList(latestSearches));
			if (latestSearchesList.contains(searchItem)) {
				latestSearchesList.remove(searchItem);
			}
		}else {
			latestSearchesList = new ArrayList<>();
		}
		latestSearchesList.add(searchItem);
		if (latestSearchesList.size() > 10) {
			latestSearchesList.remove(0);
		}
		latestSearches = latestSearchesList.toArray(new SearchHistoryItem[latestSearchesList.size()]);
		String latestSearchesString = gson.toJson(latestSearches);
		sharedPreferences.edit().putString(PREF_LATEST_SEARCHES, latestSearchesString).apply();
		return latestSearchesList;
	}

	public void clearAllRecentSearches() {
		sharedPreferences.edit().putString(PREF_LATEST_SEARCHES, "").apply();
	}

	public SearchHistoryItem[] getRecentSearches() {
		String latestSearches = sharedPreferences.getString(PREF_LATEST_SEARCHES, "");
		SearchHistoryItem[] searchItems = null;
		try{
			searchItems = gson.fromJson(latestSearches, SearchHistoryItem[].class);
		}catch (Exception e){
			Log.d("test", "getRecentSearches error: " + e.getLocalizedMessage());
		}
		return searchItems;
	}
}
