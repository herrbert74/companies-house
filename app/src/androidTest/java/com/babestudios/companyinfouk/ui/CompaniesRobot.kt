package com.babestudios.companyinfouk.ui

import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.adevinta.android.barista.assertion.BaristaBackgroundAssertions.assertHasBackground
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.babestudios.base.view.MultiStateView
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.conditionwatcher.ConditionWatcher
import com.babestudios.companyinfouk.conditionwatcher.Instruction
import com.babestudios.companyinfouk.testhelpers.matchers.RecyclerViewMatchers.hasItem
import com.babestudios.companyinfouk.testhelpers.matchers.ViewGroupMatchers
import org.hamcrest.CoreMatchers.allOf

class CompaniesRobot {

	//region Search

	fun clickSearch(): CompaniesRobot {
		val actionMenuItemView = onView(
			allOf(
				MAIN_FRAGMENT_MENU_SEARCH_MATCHER,
				withContentDescription("Search"),
				isDisplayed()
			)
		)
		actionMenuItemView.perform(click())
		return this
	}

	fun submitSearch(searchQuery: String): CompaniesRobot {
		val searchAutoComplete = onView(MAIN_FRAGMENT_SEARCH_TOOLBAR_MATCHER)
		searchAutoComplete.perform(ViewActions.replaceText(searchQuery), ViewActions.closeSoftKeyboard())
		return this
	}

	fun assertSearchResultMatchesSearchQuery(activity: AppCompatActivity): CompaniesRobot {
		ConditionWatcher.waitForCondition(SearchResultsInstruction(activity))
		InstrumentationRegistry.getInstrumentation().waitForIdleSync()
		val textView = onView(
			allOf(
				withId(R.id.lblSearchResultsCompanyName), withText("YOU  LIMITED"),
				ViewGroupMatchers.childAtPosition(
					ViewGroupMatchers.childAtPosition(
						withId(R.id.rvMainSearch),
						0
					),
					0
				),
				isDisplayed()
			)
		)
		textView.check(matches(withText("YOU  LIMITED")))
		return this
	}

	fun assertSearchResultsEmpty(activity: AppCompatActivity): CompaniesRobot {
		ConditionWatcher.waitForCondition(EmptySearchResultsInstruction(activity))
		InstrumentationRegistry.getInstrumentation().waitForIdleSync()

		assertDisplayed(R.id.tvMsvEmpty)
		return this
	}

	fun assertSearchResultsAreNotDisplayed(activity: AppCompatActivity): CompaniesRobot {
		ConditionWatcher.waitForCondition(SearchRecyclerViewShownInstruction(activity))
		InstrumentationRegistry.getInstrumentation().waitForIdleSync()

		assertHasBackground(R.id.rvMainSearch, R.color.semiTransparentBlack)
		return this
	}

	/**
	 * Checks if Search RecyclerView has results
	 */
	inner class SearchResultsInstruction(private val activity: AppCompatActivity) : Instruction() {
		override fun getDescription(): String {
			return "Wait for search results"
		}

		override fun checkCondition(): Boolean {
			val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.navHostFragmentCompanies)
			val fragments = navHostFragment?.childFragmentManager?.fragments
			fragments?.let {
				val lastFragment = navHostFragment.childFragmentManager.fragments[fragments.size - 1]
				val rv = lastFragment.view?.findViewById<RecyclerView>(R.id.rvMainSearch)
				return rv?.let { it.size > 0 } ?: false
			} ?: return false
		}
	}

	/**
	 * Checks if Search displays no results
	 */
	inner class EmptySearchResultsInstruction(private val activity: AppCompatActivity) : Instruction() {
		override fun getDescription(): String {
			return "Wait for empty view displayed"
		}

		override fun checkCondition(): Boolean {
			val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.navHostFragmentCompanies)
			val fragments = navHostFragment?.childFragmentManager?.fragments
			fragments?.let {
				val lastFragment = navHostFragment.childFragmentManager.fragments[fragments.size - 1]
				val msv = lastFragment.view?.findViewById<MultiStateView>(R.id.msvMainSearch)
				return msv?.let { it.viewState == MultiStateView.VIEW_STATE_EMPTY } ?: false
			} ?: return false
		}
	}

	/**
	 * Checks if Search RecyclerView is displayed with content
	 */
	inner class SearchRecyclerViewShownInstruction(private val activity: AppCompatActivity) : Instruction() {
		override fun getDescription(): String {
			return "Wait for empty view displayed"
		}

		override fun checkCondition(): Boolean {
			val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.navHostFragmentCompanies)
			val fragments = navHostFragment?.childFragmentManager?.fragments
			fragments?.let {
				val lastFragment = navHostFragment.childFragmentManager.fragments[fragments.size - 1]
				val msv = lastFragment.view?.findViewById<MultiStateView>(R.id.msvMainSearch)
				return msv?.let { it.viewState == MultiStateView.VIEW_STATE_CONTENT } ?: false
			} ?: return false
		}
	}

	//endregion

	//region Favourites

	fun clickFavourites(): CompaniesRobot {
		onView(MAIN_FRAGMENT_MENU_FAVOURITES_MATCHER).perform(click())
		return this
	}

	fun clickCompanyInFavourites(): CompaniesRobot {
		clickListItem(R.id.rvFavourites, 0)
		return this
	}

	fun removeFavouriteInCompany(): CompaniesRobot {
		clickOn(R.id.fabCompanyFavorite)
		return this
	}

	fun assertFavouriteIsListed(): CompaniesRobot {
		onView(FAVOURITES_FRAGMENT_RECYCLER_MATCHER)
			.check(matches(hasItem(hasDescendant(withText("Acme Painting")))))
		return this
	}

	fun assertFavouritesEmpty(activity: AppCompatActivity): CompaniesRobot {
		ConditionWatcher.waitForCondition(EmptyFavouritesInstruction(activity))
		InstrumentationRegistry.getInstrumentation().waitForIdleSync()

		assertDisplayed(R.id.tvMsvEmpty)
		return this
	}

	/**
	 * Checks if Favourites RecyclerView has no results
	 */
	inner class EmptyFavouritesInstruction(private val activity: AppCompatActivity) : Instruction() {
		override fun getDescription(): String {
			return "Wait for empty view displayed"
		}

		override fun checkCondition(): Boolean {
			val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.navHostFragmentCompanies)
			val fragments = navHostFragment?.childFragmentManager?.fragments
			fragments?.let {
				val lastFragment = navHostFragment.childFragmentManager.fragments[fragments.size - 1]
				val msv = lastFragment.view?.findViewById<MultiStateView>(R.id.msvFavourites)
				return msv?.let { it.viewState == MultiStateView.VIEW_STATE_EMPTY } ?: false
			} ?: return false
		}
	}

	//endregion

	//Company screen

	/**
	 * Checks if Company is displayed
	 */

	internal fun clickFirstRecent():CompaniesRobot {
		clickListItem(R.id.rvMainSearchHistory, 1)
		return this
	}

	internal fun clickCompanyOfficers(activity: AppCompatActivity?):CompaniesRobot {
		clickOn(R.id.llCompanyOfficers)
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
				isDisplayed()
			)
		private val FAVOURITES_FRAGMENT_RECYCLER_MATCHER = withId(R.id.rvFavourites)
	}

}