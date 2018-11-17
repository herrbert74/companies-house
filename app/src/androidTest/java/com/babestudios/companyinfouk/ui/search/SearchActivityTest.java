package com.babestudios.companyinfouk.ui.search;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.TestCompaniesHouseApplication;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;
import com.babestudios.companyinfouk.injection.AndroidTestApplicationComponent;
import com.google.gson.Gson;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Observable;

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
import static org.mockito.Mockito.when;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

	@Rule
	public ActivityTestRule<SearchActivity> mActivityTestRule = new ActivityTestRule<>(SearchActivity.class);

	@Before
	public void setUp() {
		Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
		TestCompaniesHouseApplication app = (TestCompaniesHouseApplication) instrumentation.getTargetContext().getApplicationContext();
		AndroidTestApplicationComponent component = app.getApplicationComponent();
		component.inject(this);
		Gson gson = new Gson();
		CompanySearchResult result = gson.fromJson(jsonString, CompanySearchResult.class);
		when(mActivityTestRule.getActivity().searchActivityPlugin.getPresenter().dataManager.searchCompanies("you", "0")).thenReturn(Observable.just(
				result
		));
	}

	@Test
	public void whenSearchingForYou_searchResultIsDisplayed() {
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

	String jsonString = "{\n" +
			"  \"items\": [\n" +
			"    {\n" +
			"      \"snippet\": \"\",\n" +
			"      \"address_snippet\": \"74 Lairgate, Beverley, East Yorkshire, United Kingdom, HU17 8EU\",\n" +
			"      \"description_identifier\": [\n" +
			"        \"incorporated-on\"\n" +
			"      ],\n" +
			"      \"title\": \"YOU LIMITED\",\n" +
			"      \"matches\": {\n" +
			"        \"snippet\": [],\n" +
			"        \"title\": [\n" +
			"          1,\n" +
			"          3\n" +
			"        ]\n" +
			"      },\n" +
			"      \"links\": {\n" +
			"        \"self\": \"/company/04475590\"\n" +
			"      },\n" +
			"      \"company_number\": \"04475590\",\n" +
			"      \"description\": \"04475590 - Incorporated on  2 July 2002\",\n" +
			"      \"address\": {\n" +
			"        \"country\": \"United Kingdom\",\n" +
			"        \"address_line_1\": \"Lairgate\",\n" +
			"        \"premises\": \"74\",\n" +
			"        \"locality\": \"Beverley\",\n" +
			"        \"postal_code\": \"HU17 8EU\",\n" +
			"        \"region\": \"East Yorkshire\"\n" +
			"      },\n" +
			"      \"company_status\": \"active\",\n" +
			"      \"date_of_creation\": \"2002-07-02\",\n" +
			"      \"company_type\": \"ltd\",\n" +
			"      \"kind\": \"searchresults#company\"\n" +
			"    }\n" +
			"  ],\n" +
			"  \"total_results\": 21178,\n" +
			"  \"items_per_page\": 100,\n" +
			"  \"kind\": \"search#companies\",\n" +
			"  \"page_number\": 1,\n" +
			"  \"start_index\": 0\n" +
			"}";
}
