package com.babestudios.companieshouse.ui.search;


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

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FavouritesActivityTest {

	@Rule
	public ActivityTestRule<SearchActivity> mActivityTestRule = new ActivityTestRule<>(SearchActivity.class);

	@Test
	public void favouritesActivityTest() {
		ViewInteraction actionMenuItemView = onView(
				allOf(withId(R.id.action_search), withContentDescription("Search"), isDisplayed()));
		actionMenuItemView.perform(click());

		ViewInteraction searchAutoComplete = onView(
				allOf(withId(R.id.search_src_text),
						withParent(allOf(withId(R.id.search_plate),
								withParent(withId(R.id.search_edit_frame)))),
						isDisplayed()));
		searchAutoComplete.perform(click());

		ViewInteraction searchAutoComplete2 = onView(
				allOf(withId(R.id.search_src_text),
						withParent(allOf(withId(R.id.search_plate),
								withParent(withId(R.id.search_edit_frame)))),
						isDisplayed()));
		searchAutoComplete2.perform(replaceText("john"), closeSoftKeyboard());

		ViewInteraction searchAutoComplete3 = onView(
				allOf(withId(R.id.search_src_text), withText("john"),
						withParent(allOf(withId(R.id.search_plate),
								withParent(withId(R.id.search_edit_frame)))),
						isDisplayed()));
		searchAutoComplete3.perform(pressImeActionButton());

		ViewInteraction recyclerView = onView(
				allOf(withId(R.id.search_recycler_view), isDisplayed()));
		recyclerView.perform(actionOnItemAtPosition(0, click()));

		ViewInteraction floatingActionButton = onView(
				allOf(withId(R.id.fab), isDisplayed()));
		floatingActionButton.perform(click());
		ViewInteraction textView = onView(
				allOf(withId(R.id.lblCompanyName), withText("YOU LIMITED"),
						childAtPosition(
								childAtPosition(
										withId(R.id.search_recycler_view),
										0),
								0),
						isDisplayed()));
		textView.check(matches(withText("YOU LIMITED")));
	}

}
