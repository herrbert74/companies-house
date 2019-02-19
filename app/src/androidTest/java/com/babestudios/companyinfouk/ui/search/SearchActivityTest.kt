package com.babestudios.companyinfouk.ui.search

import android.app.Instrumentation
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.espresso.ViewInteraction
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.TestCompaniesHouseApplication
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.injection.AndroidTestApplicationComponent
import com.google.gson.Gson

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import io.reactivex.Observable

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import org.mockito.Mockito.`when`

@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

	@Rule
	var mActivityTestRule = ActivityTestRule(SearchActivity::class.java)

	internal var jsonString = "{\n" +
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
			"}"

	@Before
	fun setUp() {
		val instrumentation = InstrumentationRegistry.getInstrumentation()
		val app = instrumentation.targetContext.applicationContext as TestCompaniesHouseApplication
		val component = app.applicationComponent
		//component.inject(this)
		val gson = Gson()
		val result = gson.fromJson(jsonString, CompanySearchResult::class.java)
		/*`when`(activityTestRule.activity.getSearchActivityPlugin().getPresenter().getDataManager().searchCompanies("you", "0")).thenReturn(Observable
		.just<T>(

				result
		))*/
	}

	@Test
	fun whenSearchingForYou_searchResultIsDisplayed() {
		val actionMenuItemView = onView(
				allOf(withId(R.id.action_search), withContentDescription("Search"), isDisplayed()))
		actionMenuItemView.perform(click())

		val searchAutoComplete = onView(
				allOf(withId(R.id.search_src_text),
						withParent(allOf(withId(R.id.search_plate),
								withParent(withId(R.id.search_edit_frame)))),
						isDisplayed()))
		searchAutoComplete.perform(replaceText("you"), closeSoftKeyboard())

		val searchAutoComplete2 = onView(
				allOf(withId(R.id.search_src_text), withText("you"),
						withParent(allOf(withId(R.id.search_plate),
								withParent(withId(R.id.search_edit_frame)))),
						isDisplayed()))
		searchAutoComplete2.perform(pressImeActionButton())

		val textView = onView(
				allOf(withId(R.id.lblCompanyName), withText("YOU LIMITED"),
						childAtPosition(
								childAtPosition(
										withId(R.id.search_recycler_view),
										0),
								0),
						isDisplayed()))
		textView.check(matches(withText("YOU LIMITED")))
	}

	companion object {

		fun childAtPosition(
				parentMatcher: Matcher<View>, position: Int): Matcher<View> {

			return object : TypeSafeMatcher<View>() {
				override fun describeTo(description: Description) {
					description.appendText("Child at position $position in parent ")
					parentMatcher.describeTo(description)
				}

				public override fun matchesSafely(view: View): Boolean {
					val parent = view.parent
					return (parent is ViewGroup && parentMatcher.matches(parent)
							&& view == parent.getChildAt(position))
				}
			}
		}
	}
}
