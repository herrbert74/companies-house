package com.babestudios.companyinfouk.ui.favourites

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.babestudios.companyinfouk.CompaniesRootComponent
import com.babestudios.companyinfouk.CompaniesRootContent
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
import io.mockk.coEvery
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
class FavouritesFragmentTest {

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
			)
			composeTestRule.setContent {
				CompaniesRootContent(companiesRootComponent)
			}
		}
	}

	@Test
	fun whenComingFromMain_thenShowsData() {

		composeTestRule.onNodeWithContentDescription("Favourites").performClick()
		composeTestRule.onNodeWithText("Acme Painting").assertIsDisplayed()

	}

	@Test
	fun whenComingFromMain_andFavouritesIsEmpty_thenDisplaysEmptyView() {

		coEvery {
			companiesRepository.favourites()
		} returns emptyList()

		composeTestRule.onNodeWithContentDescription("Favourites").performClick()
		composeTestRule.onNodeWithText("This list is empty").assertIsDisplayed()

	}

	@Test
	fun whenComingFromCompany_andFavouriteIsDeleted_thenDisplaysEmptyView() {

		composeTestRule.onNodeWithContentDescription("Favourites").performClick()
		composeTestRule.onNodeWithText("Acme Painting").performClick()
		composeTestRule.onNodeWithTag("Fab Favourite").performClick()

		coEvery {
			companiesRepository.favourites()
		} returns emptyList()

		Espresso.pressBack()

		composeTestRule.onNodeWithText("This list is empty").assertIsDisplayed()

	}

}
