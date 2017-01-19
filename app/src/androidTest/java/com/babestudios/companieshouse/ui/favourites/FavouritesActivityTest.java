package com.babestudios.companieshouse.ui.favourites;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.mock;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FavouritesActivityTest {

	@Rule
	public ActivityTestRule<FavouritesActivity> mActivityTestRule = new ActivityTestRule<>(FavouritesActivity.class);

	//TODO Test when coming from RecentSearchesActivity, shows data
	//TODO Test when coming from RecentSearchesActivity, not crashing on empty data
	//TODO Test when coming back from CompanyActivity after delete -> Updated

	DataManager dataManager;
	@Test
	public void favouritesActivityTest() {

		//TODO How to inject this into the activity under test?

		dataManager = mock(DataManager.class);
		SearchHistoryItem[] searchHistoryItems = new SearchHistoryItem[1];
		SearchHistoryItem searchHistoryItem = new SearchHistoryItem("Acme Painting", "12345678", 12);
		searchHistoryItems[0] = searchHistoryItem;
		Mockito.when(dataManager.getFavourites()).thenReturn(searchHistoryItems);
		FavouritesPresenter favouritesPresenter = mock(FavouritesPresenter.class);



		ViewInteraction actionMenuItemView = onView(
				allOf(withId(R.id.favourites_recycler_view), isDisplayed()));
		actionMenuItemView.perform(click());
	}

}
