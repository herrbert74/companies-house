package com.babestudios.companyinfouk.ui.main

import androidx.appcompat.app.AppCompatActivity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.babestudios.companyinfouk.companies.ui.CompaniesActivity
import com.babestudios.companyinfouk.data.di.DataContractModule
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.mock.mockCompaniesRepository
import com.babestudios.companyinfouk.ui.CompaniesRobot
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(DataContractModule::class)
@RunWith(AndroidJUnit4::class)
class MainFragmentSearchTest {

	@get:Rule(order = 0)
	val hiltAndroidRule = HiltAndroidRule(this)

	@get:Rule(order = 1)
	var activityTestRule = ActivityScenarioRule(CompaniesActivity::class.java)

	@BindValue
	val companiesRepository: CompaniesRepository = mockCompaniesRepository()

	@BindValue
	val rawResourceHelper: RawResourceHelper = mockk()

	@Before
	fun setUp() {
		hiltAndroidRule.inject()
	}

	@Test
	fun whenSearchingForYou_thenSearchResultIsDisplayed() {
		CompaniesRobot()
			.clickSearch()
			.submitSearch("you")
			.assertSearchResultMatchesSearchQuery(getActivity()!!)
	}

	@Test
	fun whenSearchResultIsEmpty_thenSearchResultEmptyIsDisplayed() {
		CompaniesRobot()
			.clickSearch()
			.submitSearch("xyzabc")
			.assertSearchResultsEmpty(getActivity()!!)
	}

	@Test
	fun whenSearchResultIsEmpty_andQueryIsCleared_thenTransparentBackgroundDisplayed() {
		CompaniesRobot()
			.clickSearch()
			.submitSearch("xyzabc")
			.assertSearchResultsEmpty(getActivity()!!)
			.submitSearch("")
			.assertSearchResultsAreNotDisplayed(getActivity()!!)
	}

	private fun getActivity(): AppCompatActivity? {
		var activity: AppCompatActivity? = null
		activityTestRule.scenario.onActivity {
			activity = it
		}
		return activity
	}

}
