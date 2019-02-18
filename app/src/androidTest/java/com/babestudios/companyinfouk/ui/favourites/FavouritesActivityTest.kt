package com.babestudios.companyinfouk.ui.favourites

import android.app.Instrumentation
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.TestCompaniesHouseApplication
import com.babestudios.companyinfouk.injection.AndroidTestApplicationComponent

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
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
		//component.inject(mActivityTestRule)
	}

	@Test
	fun whenComingFromRecentSearchesActivity_showsData() {
		onView(withRecyclerView(R.id.rvFavourites).atPositionOnView(0, R.id.lblCompanyName)).check(matches(withText("Acme Painting")))
	}

}
