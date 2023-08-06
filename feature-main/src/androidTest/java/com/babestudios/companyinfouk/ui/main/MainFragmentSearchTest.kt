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
import com.babestudios.companyinfouk.shared.root.CompaniesRootComponent
import com.babestudios.companyinfouk.main.CompaniesRootContent
import com.babestudios.companyinfouk.shared.domain.api.CompaniesDocumentRepository
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

@RunWith(AndroidJUnit4::class)
class MainFragmentSearchTest : KoinComponent {

	@get:Rule
	val composeTestRule = createComposeRule()

	private val companiesRepository: CompaniesRepository by inject()
	private val companiesDocumentRepository: CompaniesDocumentRepository by inject()
	private val mainContext: CoroutineDispatcher by inject(named("MainDispatcher"))
	private val ioContext: CoroutineDispatcher by inject(named("IoDispatcher"))

	@Before
	fun setUp() {

		CoroutineScope(mainContext).launch {
			val companiesRootComponent = CompaniesRootComponent(
				DefaultComponentContext(lifecycle = LifecycleRegistry()),
				mainContext,
				ioContext,
				companiesRepository,
				companiesDocumentRepository
			) {}
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
