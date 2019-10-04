package com.babestudios.companyinfouk.ui.search

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfo.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.injection.AndroidTestApplicationModule
import com.babestudios.companyinfouk.injection.DaggerAndroidTestApplicationComponent
import com.babestudios.companyinfouk.ui.main.MainActivity
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

	@get:Rule
	var activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

	@Before
	fun setUp() {
		val instrumentation = InstrumentationRegistry.getInstrumentation()
		val app = instrumentation.targetContext.applicationContext as CompaniesHouseApplication
		val testApplicationComponent = DaggerAndroidTestApplicationComponent.builder()
				.androidTestApplicationModule(AndroidTestApplicationModule(app))
				.build()
		app.applicationComponent = testApplicationComponent

		val jsonSearchResultForYou = testApplicationComponent.testHelper().loadJson("search_result_you")
		val companySearchItemForYou = testApplicationComponent.gson().fromJson(jsonSearchResultForYou, CompanySearchResult::class.java)
		whenever(testApplicationComponent.companiesRepository().searchCompanies(eq("you"), any())).thenReturn(Single.just(companySearchItemForYou))
		activityTestRule.launchActivity(Intent())
	}

	@Test
	fun whenSearchingForYou_thenSearchResultIsDisplayed() {
		val actionMenuItemView = onView(
				allOf(withId(R.id.action_search), withContentDescription("Search"), isDisplayed()))
		actionMenuItemView.perform(click())

		val searchAutoComplete = onView(
				allOf(withId(R.id.search_src_text),
						withParent(allOf(withId(R.id.search_plate),
								withParent(withId(R.id.search_edit_frame)))),
						isDisplayed()))
		searchAutoComplete.perform(replaceText("you"), closeSoftKeyboard())
		Thread.sleep(200L)
		val textView = onView(
				allOf(withId(R.id.lblSearchResultsCompanyName), withText("YOU  LIMITED"),
						childAtPosition(
								childAtPosition(
										withId(R.id.rvMainSearch),
										0),
								0),
						isDisplayed()))
		textView.check(matches(withText("YOU  LIMITED")))
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
