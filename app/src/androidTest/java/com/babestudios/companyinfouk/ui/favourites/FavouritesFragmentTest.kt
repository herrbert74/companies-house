package com.babestudios.companyinfouk.ui.favourites

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.babestudios.companyinfouk.companies.ui.CompaniesActivity
import com.babestudios.companyinfouk.ui.CompaniesRobot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavouritesFragmentTest {

	@Suppress("BooleanLiteralArgument")
	@Rule
	@JvmField
	var activityTestRule = ActivityTestRule(CompaniesActivity::class.java, true, false)

	//TODO Test when coming from RecentSearchesActivity, shows data
	//TODO Test when coming from RecentSearchesActivity, not crashing on empty data
	//TODO Test when coming back from CompanyActivity after delete -> Updated

	@Before
	fun setUp() {
		activityTestRule.launchActivity(Intent())
		CompaniesRobot().clickFavourites()
	}

	@Test
	fun whenComingFromRecentSearchesActivity_thenShowsData() {
		CompaniesRobot().assertFavouriteIsListed()
	}
}
