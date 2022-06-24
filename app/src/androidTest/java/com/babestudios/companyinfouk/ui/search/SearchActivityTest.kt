package com.babestudios.companyinfouk.ui.search

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
class SearchActivityTest {

	/**
	 * TODO When bad query text returns empty results, empty message is shown
	 * TODO Officer appointments to Company and then back to the original company
	 * TODO Test state transitions:
	 * empty results -> empty results
	 * empty filtered  results -> empty filtered results
	 * empty results -> empty query text -> semi transparent background
	 * any results -> change query text -> same results
	 **/
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
			.submitSearch()
			.assertSearchResultMatchesSearchQuery(getActivity()!!)
	}

	private fun getActivity(): AppCompatActivity? {
		var activity: AppCompatActivity? = null
		activityTestRule.scenario.onActivity {
			activity = it
		}
		return activity
	}

}
