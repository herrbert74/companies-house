package com.babestudios.companyinfouk.companies.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.annotations.MainThread
import com.arkivanov.mvikotlin.core.view.MviView
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.internal.PublishSubject
import com.babestudios.base.ext.navigateSafe
import com.babestudios.base.ext.textColor
import com.babestudios.base.ext.viewBinding
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.FilterAdapter
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_EMPTY
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.companies.databinding.FragmentMainBinding
import com.babestudios.companyinfouk.companies.ui.main.MainStore.State
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryAdapter
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryTypeFactory
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryVisitableBase
import com.babestudios.companyinfouk.companies.ui.main.search.SearchAdapter
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResultItem
import com.babestudios.companyinfouk.domain.model.search.FilterState
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.android.widget.textChanges

const val SEARCH_QUERY_MIN_LENGTH = 3

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), MviView<State, UserIntent> {

	private var searchHistoryAdapter: SearchHistoryAdapter? = null
	private var searchAdapter: SearchAdapter? = null

	private val viewModel: MainViewModel by viewModels()

	private val binding by viewBinding<FragmentMainBinding>()

	//Menu
	lateinit var searchMenuItem: MenuItem
	private var favouritesMenuItem: MenuItem? = null
	private var privacyMenuItem: MenuItem? = null
	private var filterMenuItem: MenuItem? = null
	private lateinit var lblSearch: TextView

	private var searchToolbarAnimationDuration: Long = 0

	//region BaseMviView This is just a copy from com.arkivanov.mvikotlin.core.view.BaseMviView,
	// as we cannot use it here and do not want to use it separately. This part could be extracted into a delegate

	private val subject = PublishSubject<UserIntent>()

	override fun events(observer: Observer<UserIntent>): Disposable = subject.subscribe(observer)

	fun sideEffects(sideEffect: SideEffect) {
		when (sideEffect) {
			is SideEffect.SearchItemClicked ->
				findNavController().navigateSafe(
					MainFragmentDirections.actionToCompany(
						sideEffect.searchHistoryItem.companyNumber,
						sideEffect.searchHistoryItem.companyName,
					)
				)
			SideEffect.ShowDeleteRecentSearchesDialog -> showDeleteRecentSearchesDialog()
		}
	}

	/**
	 * Dispatches the provided `View Event` to all subscribers
	 *
	 * @param event a `View Event` to be dispatched
	 */
	@MainThread
	fun dispatch(event: UserIntent) {
		subject.onNext(event)
	}

	override fun render(model: State) {

		if (model.isLoading) {
			binding.msvMainSearch.viewState = VIEW_STATE_LOADING
		} else if (model.error != null) {

			if (model.searchQuery == null) {
				binding.msvMainSearch.viewState = VIEW_STATE_ERROR
				val tvMsvError = binding.msvMainSearch.findViewById<TextView>(R.id.tvMsvError)
				tvMsvError.text = model.error.message
			} else {
				binding.msvMainSearchHistory.viewState = VIEW_STATE_ERROR
				val tvMsvError = binding.msvMainSearchHistory.findViewById<TextView>(R.id.tvMsvError)
				tvMsvError.text = model.error.message
			}
		} else if (model.searchQuery == null) {
			val tvMsvSearchHistoryEmpty = binding.msvMainSearchHistory.findViewById<TextView>(R.id.tvMsvEmpty)
			if (model.searchHistoryVisitables.isEmpty()) {
				binding.msvMainSearchHistory.viewState = VIEW_STATE_EMPTY
				tvMsvSearchHistoryEmpty.text = getString(R.string.no_recent_searches)
				binding.fabMainClearRecents.hide()
			} else {
				binding.fabMainClearRecents.show()
				binding.msvMainSearchHistory.viewState = VIEW_STATE_CONTENT
				showRecentSearches(model.searchHistoryVisitables)
			}
		} else {
			if (model.filteredSearchResultItems.isEmpty()) {
				showFilteredSearchEmptyState(model.searchQuery)
			} else {
				showFilteredSearchSuccess(model.filteredSearchResultItems)
			}
			showRecentSearches(model.searchHistoryVisitables)
			if (!searchMenuItem.isActionViewExpanded) {
				setSearchMenuItemExpanded(
					model.searchQuery,
					model.filterState,
				)
			}
		}

	}

	private fun showRecentSearches(searchHistoryVisitables: List<SearchHistoryVisitableBase>) {
		if (binding.rvMainSearchHistory.adapter == null) {
			searchHistoryAdapter = SearchHistoryAdapter(
				searchHistoryVisitables,
				SearchHistoryTypeFactory(),
				lifecycleScope
			)
			binding.rvMainSearchHistory.adapter = searchHistoryAdapter
			searchHistoryAdapter?.itemClicks?.onEach {
				dispatch(UserIntent.SearchHistoryItemClicked(it))
			}?.launchIn(lifecycleScope)
		} else {
			searchHistoryAdapter?.updateItems(searchHistoryVisitables)
		}
	}

	private fun showFilteredSearchSuccess(filteredSearchResultItems: List<CompanySearchResultItem>) {
		binding.fabMainClearRecents.hide()
		binding.msvMainSearch.viewState = VIEW_STATE_CONTENT
		binding.rvMainSearch.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))
		binding.msvMainSearch.viewState = VIEW_STATE_CONTENT
		if (binding.rvMainSearch.adapter == null) {
			searchAdapter = SearchAdapter(filteredSearchResultItems, lifecycleScope)
			binding.rvMainSearch.adapter = searchAdapter
			searchAdapter?.itemClicks?.onEach {
				dispatch(UserIntent.SearchItemClicked(it.title!!, it.companyNumber!!))
			}?.launchIn(lifecycleScope)
		} else {
			searchAdapter?.updateItems(filteredSearchResultItems)
		}
	}

	private fun showFilteredSearchEmptyState(searchQuery: String) {
		val tvMsvSearchEmpty = binding.msvMainSearch.findViewById<TextView>(R.id.tvMsvEmpty)
		if (searchQuery.length >= SEARCH_QUERY_MIN_LENGTH) {
			binding.msvMainSearch.viewState = VIEW_STATE_EMPTY
			tvMsvSearchEmpty.text = getString(R.string.no_search_result)
		} else {
			binding.msvMainSearch.viewState = VIEW_STATE_CONTENT
			binding.rvMainSearch.setBackgroundColor(
				ContextCompat.getColor(requireContext(), R.color.semiTransparentBlack)
			)
			searchAdapter?.updateItems(emptyList())
		}
	}

	//endregion

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		searchToolbarAnimationDuration = resources.getInteger(R.integer.search_toolbar_animation_duration).toLong()
		viewModel.onViewCreated(this, essentyLifecycle())
		initializeUI()
	}

	private fun initializeUI() {
		(activity as AppCompatActivity).setSupportActionBar(binding.tbMain)
		createSearchHistoryRecyclerView()
		createSearchRecyclerView()
		binding.fabMainClearRecents.clicks().onEach {
			dispatch(UserIntent.ClearRecentSearchesClicked)
		}.launchIn(lifecycleScope)
	}

	private fun createSearchHistoryRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvMainSearchHistory.layoutManager = linearLayoutManager
		val titlePositions = java.util.ArrayList<Int>()
		titlePositions.add(0)
		binding.rvMainSearchHistory.addItemDecoration(
			DividerItemDecorationWithSubHeading(requireContext(), titlePositions)
		)
	}

	private fun createSearchRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvMainSearch.layoutManager = linearLayoutManager
		binding.rvMainSearch.addItemDecoration(DividerItemDecoration(requireContext()))
		binding.rvMainSearch.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				dispatch(UserIntent.LoadMoreSearch(page))
			}
		})
	}

	@OptIn(FlowPreview::class)
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.search_menu, menu)
		//Filter
		filterMenuItem = menu.findItem(R.id.action_filter)
		filterMenuItem?.isVisible = false
		val spinner = filterMenuItem?.actionView as Spinner
		spinner.setBackgroundResource(0)
		spinner.setPadding(0, 0, resources.getDimensionPixelOffset(R.dimen.viewMarginLarge), 0)
		val adapter = FilterAdapter(
			requireContext(),
			resources.getStringArray(R.array.search_filter_options),
			isDropdownDarkTheme = true,
			isToolbarDarkTheme = false
		)
		spinner.adapter = adapter
		spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
				dispatch(UserIntent.SetFilterState(FilterState.values()[position]))
			}

			@Suppress("EmptyFunctionBlock")
			override fun onNothingSelected(parent: AdapterView<*>) {

			}
		}
		searchMenuItem = menu.findItem(R.id.action_search)
		searchMenuItem.setOnActionExpandListener(null)
		favouritesMenuItem = menu.findItem(R.id.action_favourites)
		favouritesMenuItem?.clicks()?.onEach {
			findNavController().navigateSafe(MainFragmentDirections.actionToFavourites())
		}?.launchIn(lifecycleScope)
		privacyMenuItem = menu.findItem(R.id.action_privacy)
		privacyMenuItem?.clicks()?.onEach {
			findNavController().navigateSafe(MainFragmentDirections.actionToPrivacy())
		}?.launchIn(lifecycleScope)
		val searchView = searchMenuItem.actionView as SearchView
		lblSearch = searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as TextView

		var searchJob: Job?
		searchJob = lblSearch.textChanges()
			.drop(1)
			.debounce(resources.getInteger(R.integer.search_input_field_debounce).toLong())
			.onEach {
				dispatch(UserIntent.SearchQueryChanged(lblSearch.text.toString()))
			}.launchIn(lifecycleScope)

		lblSearch.hint = "Search"
		lblSearch.textColor = ContextCompat.getColor(requireContext(), android.R.color.black)
		searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
			override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
				searchJob?.cancelChildren()
				if (searchMenuItem.isActionViewExpanded) {
					animateSearchToolbar(1, containsOverflow = false, show = false)
				}
				filterMenuItem?.isVisible = false
				searchMenuItem.isVisible = true
				privacyMenuItem?.isVisible = true
				favouritesMenuItem?.isVisible = true
				dispatch(UserIntent.SetSearchMenuItemCollapsed)
				return true
			}

			override fun onMenuItemActionExpand(item: MenuItem): Boolean {
				if (searchJob?.isCancelled == true) {
					searchJob = lblSearch.textChanges()
						.drop(1)
						.debounce(resources.getInteger(R.integer.search_input_field_debounce).toLong())
						.onEach {
							dispatch(UserIntent.SearchQueryChanged(lblSearch.text.toString()))
						}.launchIn(lifecycleScope)
				}
				animateSearchToolbar(1, containsOverflow = true, show = true)
				dispatch(UserIntent.SetSearchMenuItemExpanded)
				favouritesMenuItem?.isVisible = false
				privacyMenuItem?.isVisible = false
				filterMenuItem?.isVisible = true
				return true
			}
		})

	}

	/**
	 * To expand search menu item when coming back from company from search
	 */
	private fun setSearchMenuItemExpanded(queryText: String, filterState: FilterState) {
		searchMenuItem.expandActionView()
		(searchMenuItem.actionView as SearchView).setQuery(queryText, false)
		(filterMenuItem?.actionView as Spinner).setSelection(filterState.ordinal)
	}

	fun animateSearchToolbar(numberOfMenuIcon: Int, containsOverflow: Boolean, show: Boolean) {

		binding.tbMain.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))

		if (show) {
			binding.msvMainSearch.visibility = View.VISIBLE
			binding.msvMainSearch.viewState = VIEW_STATE_CONTENT
			val width = binding.tbMain.width -
				if (containsOverflow)
					resources.getDimensionPixelSize(
						R.dimen.abc_action_button_min_width_overflow_material
					)
				else
					(0) - ((resources.getDimensionPixelSize(
						R.dimen.abc_action_button_min_width_material
					) * numberOfMenuIcon) / 2)
			val createCircularReveal = ViewAnimationUtils.createCircularReveal(
				binding.tbMain,
				if (isRtl(resources)) binding.tbMain.width - width else width,
				binding.tbMain.height / 2,
				0.0f,
				width.toFloat()
			)
			createCircularReveal.duration = searchToolbarAnimationDuration
			createCircularReveal.start()
		} else {
			binding.msvMainSearch.visibility = View.GONE
			binding.fabMainClearRecents.show()
			val width = binding.tbMain.width -
				if (containsOverflow)
					resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)
				else
					0 - ((resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)
						* numberOfMenuIcon) / 2)
			val createCircularReveal = ViewAnimationUtils.createCircularReveal(
				binding.tbMain,
				if (isRtl(resources)) binding.tbMain.width - width else width,
				binding.tbMain.height / 2,
				width.toFloat(),
				0.0f
			)
			createCircularReveal.duration = searchToolbarAnimationDuration
			createCircularReveal.addListener(object : AnimatorListenerAdapter() {
				override fun onAnimationEnd(animation: Animator) {
					super.onAnimationEnd(animation)
					binding.tbMain.setBackgroundColor(
						ContextCompat.getColor(requireContext(), R.color.colorPrimary)
					)
					//activity?.invalidateOptionsMenu()
				}
			})
			createCircularReveal.start()
		}
	}

	private fun isRtl(resources: Resources): Boolean {
		return resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
	}

	private fun showDeleteRecentSearchesDialog() {
		AlertDialog.Builder(requireContext())
			.setTitle(R.string.delete_recent_searches)
			.setMessage(R.string.are_you_sure_you_want_to_delete_all_recent_searches)
			.setPositiveButton(android.R.string.ok) { _, _ -> dispatch(UserIntent.ClearRecentSearches) }
			.setNegativeButton(android.R.string.cancel) { _, _ ->
				// do nothing
			}
			.show()
	}

	//endregion

}

sealed class UserIntent {
	object ShowRecentSearches : UserIntent()
	object ClearRecentSearchesClicked : UserIntent()
	object ClearRecentSearches : UserIntent()
	data class SearchHistoryItemClicked(val searchHistoryItem: SearchHistoryItem) : UserIntent()
	data class SearchQueryChanged(val queryText: String) : UserIntent()
	data class LoadMoreSearch(val page: Int) : UserIntent()
	data class SearchItemClicked(val name: String, val number: String) : UserIntent()
	data class SetFilterState(val filterState: FilterState) : UserIntent()
	object SetSearchMenuItemExpanded : UserIntent()
	object SetSearchMenuItemCollapsed : UserIntent()
}

sealed class SideEffect {
	//Used for both history and search
	data class SearchItemClicked(val searchHistoryItem: SearchHistoryItem) : SideEffect()
	object ShowDeleteRecentSearchesDialog : SideEffect()
}
