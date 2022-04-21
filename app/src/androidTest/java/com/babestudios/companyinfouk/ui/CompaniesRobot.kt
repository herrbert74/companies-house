package com.babestudios.companyinfouk.ui

import android.content.Context
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.azimolabs.conditionwatcher.ConditionWatcher
import com.azimolabs.conditionwatcher.Instruction
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.TestCompaniesHouseApplication
import com.babestudios.companyinfouk.testhelpers.matchers.RecyclerViewMatchers.hasItem
import com.babestudios.companyinfouk.testhelpers.matchers.ViewGroupMatchers
import org.hamcrest.CoreMatchers.allOf

class CompaniesRobot {
	private val targetContext: Context by lazy {
		InstrumentationRegistry.getInstrumentation().targetContext
	}

	//region Search

	fun clickSearch(): CompaniesRobot {
		val actionMenuItemView = onView(
				allOf(MAIN_FRAGMENT_MENU_SEARCH_MATCHER,
						withContentDescription("Search"),
						isDisplayed()))
		actionMenuItemView.perform(click())
		return this
	}

	fun submitSearch(): CompaniesRobot {
		val searchAutoComplete = onView(MAIN_FRAGMENT_SEARCH_TOOLBAR_MATCHER)
		searchAutoComplete.perform(ViewActions.replaceText("you"), ViewActions.closeSoftKeyboard())
		return this
	}

	fun assertSearchResultMatchesSearchQuery(): CompaniesRobot {
		ConditionWatcher.waitForCondition(SearchResultsInstruction())
		val textView = onView(
				allOf(withId(R.id.lblSearchResultsCompanyName), withText("YOU  LIMITED"),
						ViewGroupMatchers.childAtPosition(
								ViewGroupMatchers.childAtPosition(
										withId(R.id.rvMainSearch),
										0),
								0),
						isDisplayed()))
		textView.check(matches(withText("YOU  LIMITED")))
		return this
	}

	/**
	 * Checks if RecyclerView has results or not
	 */
	inner class SearchResultsInstruction : Instruction() {
		override fun getDescription(): String {
			return "Wait for search results"
		}

		override fun checkCondition(): Boolean {
			val activity = (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
					as TestCompaniesHouseApplication).getCurrentActivity()
					?: return false
			val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.navHostFragmentCompanies)
			val fragments = navHostFragment?.childFragmentManager?.fragments
			fragments?.let {
				val lastFragment = navHostFragment.childFragmentManager.fragments[fragments.size - 1]
				val rv = lastFragment.view?.findViewById<RecyclerView>(R.id.rvMainSearch)
				return rv?.let { it.size > 0 } ?: false
			} ?: return false
		}
	}

	//endregion

	//region Favourites

	fun clickFavourites(): CompaniesRobot {
		onView(MAIN_FRAGMENT_MENU_FAVOURITES_MATCHER).perform(click())
		return this
	}

	fun assertFavouriteIsListed(): CompaniesRobot {
		onView(FAVOURITES_FRAGMENT_RECYCLER_MATCHER)
				.check(matches(hasItem(hasDescendant(withText("Acme Painting")))))
		return this
	}

	//endregion

	companion object {
		private val MAIN_FRAGMENT_MENU_FAVOURITES_MATCHER = withId(R.id.action_favourites)
		private val MAIN_FRAGMENT_MENU_SEARCH_MATCHER = withId(R.id.action_search)
		private val MAIN_FRAGMENT_SEARCH_TOOLBAR_MATCHER =
				allOf(
						withId(R.id.search_src_text),
						withParent(
								allOf(
										withId(R.id.search_plate),
										withParent(withId(R.id.search_edit_frame))
								)
						),
						isDisplayed())
		private val FAVOURITES_FRAGMENT_RECYCLER_MATCHER = withId(R.id.rvFavourites)
	}

}