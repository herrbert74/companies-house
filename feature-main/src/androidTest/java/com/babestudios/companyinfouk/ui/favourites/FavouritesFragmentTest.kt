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
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.babestudios.companyinfouk.shared.root.CompaniesRootComponent
import com.babestudios.companyinfouk.main.CompaniesRootContent
import com.babestudios.companyinfouk.shared.root.Configuration
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.mock.mockWithEmptyFavourites
import com.babestudios.companyinfouk.mock.mockWithFavourites
import com.babestudios.companyinfouk.shared.root.navigation
import com.babestudios.companyinfouk.shared.domain.api.CompaniesDocumentRepository
import io.mockk.coEvery
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
class FavouritesFragmentTest : KoinComponent {

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
	fun whenComingFromMain_thenShowsData() {

		//Reset
		companiesRepository.mockWithFavourites()
		CoroutineScope(mainContext).launch {
			navigation.popWhile { topOfStack: Configuration -> topOfStack !is Configuration.Main }
		}

		composeTestRule.onNodeWithContentDescription("Favourites").performClick()
		composeTestRule.onNodeWithText("Acme Painting").assertIsDisplayed()

	}

	@Test
	fun whenComingFromMain_andFavouritesIsEmpty_thenDisplaysEmptyView() {

		//Reset
		CoroutineScope(mainContext).launch {
			navigation.popWhile { topOfStack: Configuration -> topOfStack !is Configuration.Main }
		}

		companiesRepository.mockWithEmptyFavourites()

		composeTestRule.onNodeWithContentDescription("Favourites").performClick()
		composeTestRule.onNodeWithText("This list is empty").assertIsDisplayed()

	}

	@Test
	fun whenComingFromCompany_andFavouriteIsDeleted_thenDisplaysEmptyView() {

		//Reset
		companiesRepository.mockWithFavourites()
		CoroutineScope(mainContext).launch {
			navigation.popWhile { topOfStack: Configuration -> topOfStack !is Configuration.Main }
		}

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
