package com.babestudios.companyinfouk.companies.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.babestudios.base.ext.biLet
import com.babestudios.base.ext.textColor
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.FilterAdapter
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.companies.ui.CompaniesState
import com.babestudios.companyinfouk.companies.ui.CompaniesViewModel
import com.babestudios.companyinfouk.companies.ui.FilterState
import com.babestudios.companyinfouk.companies.ui.main.recents.AbstractSearchHistoryVisitable
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryAdapter
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryTypeFactory
import com.babestudios.companyinfouk.companies.ui.main.search.*
import com.jakewharton.rxbinding2.view.RxMenuItem
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.concurrent.TimeUnit


class MainFragment : BaseMvRxFragment() {

	private var searchHistoryAdapter: SearchHistoryAdapter? = null
	private var searchAdapter: SearchAdapter? = null

	private val viewModel by activityViewModel(CompaniesViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//Menu
	lateinit var searchMenuItem: MenuItem
	private var favouritesMenuItem: MenuItem? = null
	private var privacyMenuItem: MenuItem? = null
	private var filterMenuItem: MenuItem? = null
	private var lblSearch: TextView? = null
	private var flagDoNotAnimateSearchMenuItem = false
	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {

		return inflater.inflate(R.layout.fragment_main, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(tbMain)
		createSearchHistoryRecyclerView()
		createSearchRecyclerView()
		selectSubscribes()
		viewModel.showRecentSearches()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	private fun createSearchHistoryRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		rvMainSearchHistory.layoutManager = linearLayoutManager
		val titlePositions = java.util.ArrayList<Int>()
		titlePositions.add(0)
		rvMainSearchHistory.addItemDecoration(DividerItemDecorationWithSubHeading(requireContext(), titlePositions))
	}

	private fun createSearchRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		rvMainSearch?.layoutManager = linearLayoutManager
		rvMainSearch.addItemDecoration(DividerItemDecoration(requireContext()))
		rvMainSearch.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				viewModel.loadMoreSearch(page)
			}
		})
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.search_menu, menu)
		//Filter
		filterMenuItem = menu.findItem(R.id.action_filter)
		filterMenuItem?.isVisible = false
		val spinner = filterMenuItem?.actionView as Spinner
		spinner.setBackgroundResource(0)
		spinner.setPadding(0, 0, resources.getDimensionPixelOffset(R.dimen.view_margin), 0)
		val adapter = FilterAdapter(
				requireContext(),
				resources.getStringArray(R.array.search_filter_options),
				isDropdownDarkTheme = true,
				isToolbarDarkTheme = false
		)
		spinner.adapter = adapter
		spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
				viewModel.setFilterState(FilterState.values()[position])
			}

			override fun onNothingSelected(parent: AdapterView<*>) {

			}
		}
		searchMenuItem = menu.findItem(R.id.action_search)
		favouritesMenuItem = menu.findItem(R.id.action_favourites)
		privacyMenuItem = menu.findItem(R.id.action_privacy)
		val searchView = searchMenuItem.actionView as SearchView
		lblSearch = searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as TextView?
		observeActions()
		lblSearch?.hint = "Search"
		lblSearch?.textColor = ContextCompat.getColor(requireContext(), android.R.color.black)
		searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
			override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
				if (searchMenuItem.isActionViewExpanded) {
					animateSearchToolbar(1, containsOverflow = false, show = false)
				}
				filterMenuItem?.isVisible = false
				searchMenuItem.isVisible = true
				privacyMenuItem?.isVisible = true
				favouritesMenuItem?.isVisible = true
				viewModel.setSearchMenuItemExpanded(false)
				return true
			}

			override fun onMenuItemActionExpand(item: MenuItem): Boolean {
				if (!flagDoNotAnimateSearchMenuItem) {
					animateSearchToolbar(1, containsOverflow = true, show = true)
					viewModel.setSearchMenuItemExpanded(true)
				} else {
					flagDoNotAnimateSearchMenuItem = false
				}
				favouritesMenuItem?.isVisible = false
				privacyMenuItem?.isVisible = false
				filterMenuItem?.isVisible = true
				return true
			}
		})
		withState(viewModel) { state ->
			//For when invalidateOptionsMenu called
			if (state.isSearchMenuItemExpanded) {
				searchMenuItem.expandActionView()
				(searchMenuItem.actionView as SearchView).setQuery(state.queryText, false)
				(filterMenuItem?.actionView as Spinner).setSelection(state.filterState.ordinal)
				flagDoNotAnimateSearchMenuItem = true
			}
			//For when we are recovering after a process death
			if (state.queryText.isNotEmpty()) {
				Handler().postDelayed({ //To avoid skipping initial state in this case : we want to reload it
					searchMenuItem.expandActionView()
					(filterMenuItem?.actionView as Spinner).setSelection(state.filterState.ordinal)
					lblSearch?.text = state.queryText
				}, 300)
			}
		}
	}

	@SuppressLint("PrivateResource")
	fun animateSearchToolbar(numberOfMenuIcon: Int, containsOverflow: Boolean, show: Boolean) {

		tbMain.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))

		if (show) {
			msvMainSearch.visibility = View.VISIBLE
			msvMainSearch.viewState = VIEW_STATE_CONTENT
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				val width = tbMain.width -
						if (containsOverflow) resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) else (0) -
								((resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2)
				val createCircularReveal = ViewAnimationUtils.createCircularReveal(tbMain,
						if (isRtl(resources)) tbMain.width - width else width, tbMain.height / 2, 0.0f, width.toFloat())
				createCircularReveal.duration = 250
				createCircularReveal.start()
			} else {
				val translateAnimation = TranslateAnimation(0.0f, 0.0f, ((-tbMain.height).toFloat()), 0.0f)
				translateAnimation.duration = 220
				tbMain.clearAnimation()
				tbMain.startAnimation(translateAnimation)
			}
		} else {
			msvMainSearch.visibility = View.GONE
			fabMainSearch.show()
			viewModel.clearSearch()
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				val width = tbMain.width -
						if (containsOverflow) resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) else 0 -
								((resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2)
				val createCircularReveal = ViewAnimationUtils.createCircularReveal(tbMain,
						if (isRtl(resources)) tbMain.width - width else width, tbMain.height / 2, width.toFloat(), 0.0f)
				createCircularReveal.duration = 250
				createCircularReveal.addListener(object : AnimatorListenerAdapter() {
					override fun onAnimationEnd(animation: Animator) {
						super.onAnimationEnd(animation)
						tbMain.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
						activity?.invalidateOptionsMenu()
					}
				})
				createCircularReveal.start()
			} else {
				val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
				val translateAnimation = TranslateAnimation(0.0f, 0.0f, 0.0f, -tbMain.height.toFloat())
				val animationSet = AnimationSet(true)
				animationSet.addAnimation(alphaAnimation)
				animationSet.addAnimation(translateAnimation)
				animationSet.duration = 220
				animationSet.setAnimationListener(object : Animation.AnimationListener {
					override fun onAnimationStart(animation: Animation) {

					}

					override fun onAnimationEnd(animation: Animation) {
						tbMain.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
						activity?.invalidateOptionsMenu()
					}

					override fun onAnimationRepeat(animation: Animation) {

					}
				})
				tbMain.startAnimation(animationSet)
			}
		}
	}

	@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private fun isRtl(resources: Resources): Boolean {
		return resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
	}

	//endregion

	//region render

	override fun invalidate() {
	}

	private fun selectSubscribes() {
		viewModel.selectSubscribe(CompaniesState::searchRequest) { searchRequest ->
			val tvMsvSearchError = msvMainSearch.findViewById<TextView>(R.id.tvMsvError)
			when (searchRequest) {
				is Loading -> {
					msvMainSearch.viewState = VIEW_STATE_LOADING
					observeActions()
				}
				is Fail -> {
					msvMainSearch.viewState = VIEW_STATE_ERROR
					tvMsvSearchError.text = searchRequest.error.message
				}
				is Success -> {
					//FilteredSearchVisitables will deal with non empty states
					if (searchRequest.invoke().items.isEmpty()) {
						showFilteredSearch()
					}
				}
			}
		}

		viewModel.selectSubscribe(CompaniesState::filteredSearchVisitables) {
			showFilteredSearch()
		}

		viewModel.selectSubscribe(CompaniesState::searchHistoryRequest) { searchHistoryRequest ->
			val tvMsvSearchHistoryError = msvMainSearchHistory.findViewById<TextView>(R.id.tvMsvError)
			val tvMsvSearchHistoryEmpty = msvMainSearchHistory.findViewById<TextView>(R.id.tvMsvEmpty)
			withState(viewModel) { state ->
				when (searchHistoryRequest) {
					is Success -> {
						if (state.searchHistoryVisitables.isNullOrEmpty()) {
							msvMainSearchHistory.viewState = VIEW_STATE_EMPTY
							tvMsvSearchHistoryEmpty.text = getString(R.string.no_recent_searches)
							fabMainSearch.hide()
						} else {
							fabMainSearch.show()
							state.searchHistoryVisitables.let {
								msvMainSearchHistory.viewState = VIEW_STATE_CONTENT
								if (rvMainSearchHistory?.adapter == null) {
									searchHistoryAdapter = SearchHistoryAdapter(it, SearchHistoryTypeFactory())
									rvMainSearchHistory?.adapter = searchHistoryAdapter
								} else {
									searchHistoryAdapter?.updateItems(it)
								}
								observeActions()
							}
						}
					}
					is Fail -> {
						msvMainSearch.viewState = VIEW_STATE_ERROR
						tvMsvSearchHistoryError.text = searchHistoryRequest.error.message
					}
				}
			}
		}
	}

	private fun showFilteredSearch() {
		val tvMsvSearchEmpty = msvMainSearch.findViewById<TextView>(R.id.tvMsvEmpty)
		fabMainSearch.hide()
		withState(viewModel) { state ->
			if (state.queryText.length >= 3 && state.filteredSearchVisitables.isEmpty()) {
				msvMainSearch.viewState = VIEW_STATE_EMPTY
				tvMsvSearchEmpty.text = getString(R.string.no_search_result)
				observeActions()
			} else {
				msvMainSearch.viewState = VIEW_STATE_CONTENT
				if (state.queryText.length > 2) {
					rvMainSearch.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))
					viewModel.logSearch()
				} else
					rvMainSearch.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent_black))

				state.filteredSearchVisitables.let {
					rvMainSearch.visibility = View.VISIBLE
					msvMainSearch.viewState = VIEW_STATE_CONTENT
					if (rvMainSearch?.adapter == null) {
						searchAdapter = SearchAdapter(it, SearchTypeFactory())
						rvMainSearch?.adapter = searchAdapter
					} else {
						searchAdapter?.updateItems(it)
					}
					observeActions()
				}
			}
		}
	}

	private fun showDeleteRecentSearchesDialog() {
		AlertDialog.Builder(requireContext())
				.setTitle(R.string.delete_recent_searches)
				.setMessage(R.string.are_you_sure_you_want_to_delete_all_recent_searches)
				.setPositiveButton(android.R.string.yes) { _, _ -> viewModel.clearAllRecentSearches() }
				.setNegativeButton(android.R.string.no) { _, _ ->
					// do nothing
				}
				.show()
		observeActions()
	}

//endregion

//region events

	private fun observeActions() {
		eventDisposables.clear()
		lblSearch?.let {
			RxTextView.textChanges(it)
					.skipInitialValue()
					.debounce(500, TimeUnit.MILLISECONDS)
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe {
						viewModel.onSearchQueryChanged(lblSearch?.text.toString())
					}
					?.let { queryTextChangeDisposable -> eventDisposables.add(queryTextChangeDisposable) }
		}
		searchHistoryAdapter?.getViewClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<AbstractSearchHistoryVisitable> ->
					viewModel.searchHistoryItemClicked(view.adapterPosition)
				}
				?.let { eventDisposables.add(it) }
		searchAdapter?.getViewClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<AbstractSearchVisitable> ->
					withState(viewModel) { state ->
						state.filteredSearchVisitables.let { searchItems ->
							val searchItem = (searchItems[(view as SearchViewHolder).adapterPosition] as SearchVisitable).searchItem
							(searchItem.title to searchItem.companyNumber).biLet { title, number ->
								viewModel.searchItemClicked(title, number)
							}
						}
					}
				}
				?.let { eventDisposables.add(it) }
		favouritesMenuItem?.let {
			RxMenuItem.clicks(it)
					.take(1)
					.subscribe {
						viewModel.companiesNavigator.mainToFavourites()
					}
					.also { disposable -> eventDisposables.add(disposable) }
		}
		privacyMenuItem?.let {
			RxMenuItem.clicks(it)
					.take(1)
					.subscribe {
						viewModel.companiesNavigator.mainToPrivacy()
					}
					.also { disposable -> eventDisposables.add(disposable) }
		}
		fabMainSearch?.let {
			RxView.clicks(it)
					.take(1)
					.subscribe {
						withState(viewModel) { state ->
							if (state.searchHistoryRequest is Success) {
								showDeleteRecentSearchesDialog()
							}
						}
					}
					.also { disposable -> eventDisposables.add(disposable) }
		}
	}

//endregion
}