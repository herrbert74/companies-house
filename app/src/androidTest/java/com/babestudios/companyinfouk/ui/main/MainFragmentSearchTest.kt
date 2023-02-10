package com.babestudios.companyinfouk.ui.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
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
class MainFragmentSearchTest {

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
	fun whenSearchingForYou_thenSearchResultIsDisplayed() {

		composeTestRule.onNodeWithContentDescription("Search icon").performClick()
		composeTestRule.onNode(hasImeAction(ImeAction.Search)).performTextInput("you")
		composeTestRule.onNodeWithText("YOU  LIMITED").assertIsDisplayed()

	}

	@Test
	fun whenSearchResultIsEmpty_thenSearchResultEmptyIsDisplayed() {

		composeTestRule.onNodeWithContentDescription("Search icon").performClick()
		composeTestRule.onNode(hasImeAction(ImeAction.Search)).performTextInput("xyzabc")

		composeTestRule
			.onNodeWithText("Your search did not return any companies. Please try another one")
			.assertIsDisplayed()

	}

	/**
	 * We need to set a separate colour for SearchBar and its content to allow semi transparent content,
	but it's not currently possible.
	 */
//	@Test
//	fun whenSearchResultIsEmpty_andQueryIsCleared_thenTransparentBackgroundDisplayed() {
//		composeTestRule.onNodeWithContentDescription("Search icon").performClick()
//		composeTestRule.onNode(hasImeAction(ImeAction.Search)).performTextInput("xyzabc")
//
//		composeTestRule
//			.onNodeWithText("Your search did not return any companies. Please try another one")
//			.assertIsDisplayed()
//	}

}
