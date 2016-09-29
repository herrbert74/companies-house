package com.babestudios.companieshouse.data.local;


import android.content.Context;
import android.content.SharedPreferences;

import com.babestudios.companieshouse.data.model.SearchItem;
import com.babestudios.companieshouse.injection.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {
	private final SharedPreferences sharedPreferences;
	private final Gson gson;

	private static final String PREF_FILE_NAME = "companies_house_pref_file";
	private static final String PREF_LATEST_SEARCHES = "companies_house_latest_searches";

	@Inject
	public PreferencesHelper(@ApplicationContext Context context) {
		sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
		gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
				.create();
	}

	public void putLatestSearch(SearchItem searchItem) {
		SearchItem[] latestSearches = getLatestSearches();
	}

	public SearchItem[] getLatestSearches() {
		String latestSearches = sharedPreferences.getString(PREF_LATEST_SEARCHES, "");
		return new Gson().fromJson(latestSearches, SearchItem[].class);
	}
}
