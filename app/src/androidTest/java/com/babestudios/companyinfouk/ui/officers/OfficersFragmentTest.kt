package com.babestudios.companyinfouk.ui.officers

import androidx.appcompat.app.AppCompatActivity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.companies.ui.CompaniesActivity
import com.babestudios.companyinfouk.data.di.DataContractModule
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.mock.mockCompaniesRepository
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
class OfficersFragmentTest {

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
	fun whenDisplayingOfficers_andCompanyClicked_thenShowCompany() {
		clickListItem(R.id.rvMainSearchHistory, 1)
		clickOn(R.id.llCompanyOfficers)
		clickListItem(R.id.rvOfficers, 0)
		clickOn(R.id.btnOfficerDetailsAppointments)
		clickListItem(R.id.rvOfficerAppointments, 1)
		assertDisplayed("04475590")
	}

	private fun getActivity(): AppCompatActivity? {
		var activity: AppCompatActivity? = null
		activityTestRule.scenario.onActivity {
			activity = it
		}
		return activity
	}

}
