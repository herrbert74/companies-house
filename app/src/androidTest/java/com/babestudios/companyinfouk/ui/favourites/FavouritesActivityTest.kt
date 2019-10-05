package com.babestudios.companyinfouk.ui.favourites

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.injection.AndroidTestApplicationModule
import com.babestudios.companyinfouk.injection.DaggerAndroidTestApplicationComponent
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class FavouritesActivityTest {

	@Suppress("BooleanLiteralArgument")
	@Rule
	@JvmField
	var activityTestRule = ActivityTestRule(FavouritesActivity::class.java, true, false)

	//TODO Test when coming from RecentSearchesActivity, shows data
	//TODO Test when coming from RecentSearchesActivity, not crashing on empty data
	//TODO Test when coming back from CompanyActivity after delete -> Updated

	@Before
	fun setUp() {
		val instrumentation = InstrumentationRegistry.getInstrumentation()
		val app = instrumentation.targetContext.applicationContext as CompaniesHouseApplication
		val testApplicationComponent = DaggerAndroidTestApplicationComponent.builder()
				.androidTestApplicationModule(AndroidTestApplicationModule(app))
				.build()
		val favourites = Array(1){ SearchHistoryItem("Acme Painting", "1", 111L) }
		app.applicationComponent = testApplicationComponent
		whenever(testApplicationComponent.companiesRepository().favourites).thenReturn(favourites)
		activityTestRule.launchActivity(Intent())

	}

	@Test
	fun whenComingFromRecentSearchesActivity_thenShowsData() {
		onView(ViewMatchers.withId(R.id.rvFavourites)).check(matches(hasItem(hasDescendant(withText("Acme Painting")))))
	}

	private fun hasItem(matcher: Matcher<View>): Matcher<View> {
		return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

			override fun describeTo(description: Description) {
				description.appendText("has item: ")
				matcher.describeTo(description)
			}

			override fun matchesSafely(view: RecyclerView): Boolean {
				val adapter = view.adapter
				for (position in 0 until adapter!!.itemCount) {
					val type = adapter.getItemViewType(position)
					val holder = adapter.createViewHolder(view, type)
					adapter.onBindViewHolder(holder, position)
					if (matcher.matches(holder.itemView)) {
						return true
					}
				}
				return false
			}
		}
	}
}
