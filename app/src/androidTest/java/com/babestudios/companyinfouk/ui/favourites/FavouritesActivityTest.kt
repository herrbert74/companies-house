package com.babestudios.companyinfouk.ui.favourites

import android.app.Instrumentation
import android.support.test.InstrumentationRegistry
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.TestCompaniesHouseApplication
import com.babestudios.companyinfouk.injection.AndroidTestApplicationComponent

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.babestudios.companyinfouk.TestUtils.withRecyclerView

@LargeTest
@RunWith(AndroidJUnit4::class)
class FavouritesActivityTest {

	@Rule
	var mActivityTestRule = ActivityTestRule(FavouritesActivity::class.java)

	//TODO Test when coming from RecentSearchesActivity, shows data
	//TODO Test when coming from RecentSearchesActivity, not crashing on empty data
	//TODO Test when coming back from CompanyActivity after delete -> Updated

	@Before
	fun setUp() {
		val instrumentation = InstrumentationRegistry.getInstrumentation()
		val app = instrumentation.targetContext.applicationContext as TestCompaniesHouseApplication
		val component = app.applicationComponent
		component.inject(mActivityTestRule)
	}

	@Test
	fun whenComingFromRecentSearchesActivity_showsData() {
		onView(withRecyclerView(R.id.rvFavourites).atPositionOnView(0, R.id.lblCompanyName)).check(matches(withText("Acme Painting")))
	}

}
