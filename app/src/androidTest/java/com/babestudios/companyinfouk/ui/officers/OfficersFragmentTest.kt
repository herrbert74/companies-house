package com.babestudios.companyinfouk.ui.officers

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.babestudios.companyinfouk.companies.ui.CompaniesRootComponent
import com.babestudios.companyinfouk.companies.ui.CompaniesRootContent
import com.babestudios.companyinfouk.companies.ui.Configuration
import com.babestudios.companyinfouk.companies.ui.main.MainExecutor
import com.babestudios.companyinfouk.data.di.DataContractModule
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.mock.mockCompaniesRepository
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
	val composeTestRule = createComposeRule()

	@Inject
	lateinit var mainExecutor: MainExecutor

	@BindValue
	val companiesRepository: CompaniesRepository = mockCompaniesRepository()

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	@Inject
	@IoDispatcher
	lateinit var ioContext: CoroutineDispatcher

	@BindValue
	val rawResourceHelper: RawResourceHelper = mockk()

	@Before
	fun setUp() {
		hiltAndroidRule.inject()
		CoroutineScope(mainContext).launch {
			val companiesRootComponent = CompaniesRootComponent(
				DefaultComponentContext(lifecycle = LifecycleRegistry()),
				mainContext,
				ioContext,
				companiesRepository,
				{},
				mainExecutor,
				{},
				Configuration.Main,
				{},
			)
			composeTestRule.setContent {
				CompaniesRootContent(companiesRootComponent)
			}
		}
	}

	/**
	 * Compose UI testing is not possible across features until #125 (Remove Fragments) is fixed.
	 */
	@Test
	fun whenDisplayingOfficers_andCompanyClicked_thenShowCompany() {

		composeTestRule.onNodeWithText("Acme Painting").onParent().performClick()
		composeTestRule.onNodeWithText("Officers").performClick()
		composeTestRule.waitForIdle()
		composeTestRule.onAllNodes(hasClickAction()).onFirst().performClick()
		composeTestRule.onNodeWithText("Appointments").onParent().performClick()

//		clickListItem(R.id.rvMainSearchHistory, 1)
//		clickOn(R.id.llCompanyOfficers)
//		clickListItem(R.id.rvOfficers, 0)
//		clickOn(R.id.btnOfficerDetailsAppointments)
//		clickListItem(R.id.rvOfficerAppointments, 1)
//		assertDisplayed("04475590")

	}

}
