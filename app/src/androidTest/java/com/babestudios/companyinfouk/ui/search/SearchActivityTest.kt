package com.babestudios.companyinfouk.ui.search

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.companies.ui.CompaniesActivity
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.ui.CompaniesRobot
import io.mockk.every
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
	@Suppress("BooleanLiteralArgument")
	@Rule
	@JvmField
	var activityTestRule = ActivityTestRule(CompaniesActivity::class.java, true, false)

	@Before
	fun setUp() {
		val instrumentation = InstrumentationRegistry.getInstrumentation()
		val app = instrumentation.targetContext.applicationContext as CompaniesHouseApplication
		val comp = app.provideCoreComponent() as AndroidTestCoreComponent

		val jsonSearchResultForYou = this.loadJson("search_result_you")
		val companySearchItemForYou = comp.gson().fromJson(jsonSearchResultForYou, CompanySearchResult::class.java)
		every {
			comp.companiesRepository().searchCompanies(eq("you"), any())
		} returns (Single.just(companySearchItemForYou))
		activityTestRule.launchActivity(Intent())
	}

	@Test
	fun whenSearchingForYou_thenSearchResultIsDisplayed() {
		CompaniesRobot()
			.clickSearch()
			.submitSearch()
			.assertSearchResultMatchesSearchQuery()
	}

}
