package com.babestudios.companyinfouk.ui.officers

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.babestudios.companyinfouk.main.CompaniesRootContent
import com.babestudios.companyinfouk.shared.domain.api.CompaniesDocumentRepository
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.root.CompaniesRootComponent
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
class OfficersFragmentTest : KoinComponent {

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
	fun whenDisplayingOfficers_andCompanyClicked_thenShowCompany() {
		composeTestRule.onNodeWithText("Acme Painting").onParent().performClick()
		composeTestRule.onNodeWithTag("Company Screen Column").performScrollToNode(hasText("Officers"))
		composeTestRule.onNodeWithText("Officers").performClick()
		composeTestRule.onAllNodes(hasClickAction()).onFirst().performClick()
		composeTestRule.onNodeWithTag("OfficerDetailsColumn").performScrollToNode(hasText("Appointments", ignoreCase = true))
		composeTestRule.onNodeWithText("Appointments", ignoreCase = true).performClick()
		composeTestRule.onAllNodes(hasClickAction()).onFirst().performClick()
		composeTestRule.onNodeWithText("04475590").assertIsDisplayed()

	}

}
