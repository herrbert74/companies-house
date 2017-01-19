package com.babestudios.companieshouse.ui.search;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.babestudios.companieshouse.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

	@Rule
	public ActivityTestRule<SearchActivity> mActivityTestRule = new ActivityTestRule<>(SearchActivity.class);

	@Test
	public void searchActivityTest() {
		ViewInteraction actionMenuItemView = onView(
				allOf(withId(R.id.action_search), withContentDescription("Search"), isDisplayed()));
		actionMenuItemView.perform(click());

		ViewInteraction searchAutoComplete = onView(
				allOf(withId(R.id.search_src_text),
						withParent(allOf(withId(R.id.search_plate),
								withParent(withId(R.id.search_edit_frame)))),
						isDisplayed()));
		searchAutoComplete.perform(replaceText("you"), closeSoftKeyboard());

		ViewInteraction searchAutoComplete2 = onView(
				allOf(withId(R.id.search_src_text), withText("you"),
						withParent(allOf(withId(R.id.search_plate),
								withParent(withId(R.id.search_edit_frame)))),
						isDisplayed()));
		searchAutoComplete2.perform(pressImeActionButton());

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

	public static Matcher<View> childAtPosition(
			final Matcher<View> parentMatcher, final int position) {

		return new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("Child at position " + position + " in parent ");
				parentMatcher.describeTo(description);
			}

			@Override
			public boolean matchesSafely(View view) {
				ViewParent parent = view.getParent();
				return parent instanceof ViewGroup && parentMatcher.matches(parent)
						&& view.equals(((ViewGroup) parent).getChildAt(position));
			}
		};
	}
}
