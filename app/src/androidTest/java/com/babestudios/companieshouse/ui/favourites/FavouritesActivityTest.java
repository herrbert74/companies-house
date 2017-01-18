package com.babestudios.companieshouse.ui.favourites;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.babestudios.companieshouse.ui.search.SearchActivityTest.childAtPosition;
import static org.hamcrest.Matchers.allOf;

import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.ui.favourites.FavouritesActivity;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FavouritesActivityTest {

	@Rule
	public ActivityTestRule<FavouritesActivity> mActivityTestRule = new ActivityTestRule<>(FavouritesActivity.class);

	@Test
	public void favouritesActivityTest() {
		ViewInteraction actionMenuItemView = onView(
				allOf(withId(R.id.favourites_recycler_view), isDisplayed()));
		actionMenuItemView.perform(click());
	}

}
