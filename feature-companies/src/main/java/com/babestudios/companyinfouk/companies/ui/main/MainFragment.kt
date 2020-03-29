package com.babestudios.companyinfouk.companies.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.FilterAdapter
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.companies.databinding.FragmentMainBinding
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
import java.util.concurrent.TimeUnit

const val SEARCH_QUERY_MIN_LENGTH = 3

class MainFragment : BaseMvRxFragment() {

	private var searchHistoryAdapter: SearchHistoryAdapter? = null
	private var searchAdapter: SearchAdapter? = null

	private val viewModel by activityViewModel(CompaniesViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentMainBinding? = null
	private val binding get() = _binding!!
	
	//Menu
	lateinit var searchMenuItem: MenuItem
	private var favouritesMenuItem: MenuItem? = null
	private var privacyMenuItem: MenuItem? = null
	private var filterMenuItem: MenuItem? = null
	private var lblSearch: TextView? = null
	private var flagDoNotAnimateSearchMenuItem = false

	private var searchToolbarAnimationDuration : Long = 0

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		_binding = FragmentMainBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		searchToolbarAnimationDuration =
		resources.getInteger(R.integer.search_toolbar_animation_duration).toLong()
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.tbMain)
		createSearchHistoryRecyclerView()
		createSearchRecyclerView()
		selectSubscribes()
		viewModel.showRecentSearches()
	}

	override fun onResume() {
		super.onResume()
		Log.d("logCatText", "onResume: ")
		observeActions()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun createSearchHistoryRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvMainSearchHistory.layoutManager = linearLayoutManager
		val titlePositions = java.util.ArrayList<Int>()
		titlePositions.add(0)
		binding.rvMainSearchHistory.addItemDecoration(DividerItemDecorationWithSubHeading(requireContext(), titlePositions))
	}

	private fun createSearchRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvMainSearch.layoutManager = linearLayoutManager
		binding.rvMainSearch.addItemDecoration(DividerItemDecoration(requireContext()))
		binding.rvMainSearch.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
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
		spinner.setPadding(0, 0, resources.getDimensionPixelOffset(R.dimen.screenMargin), 0)
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

			@Suppress("EmptyFunctionBlock")
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
				Handler().postDelayed({
					//To avoid skipping initial state in this case : we want to reload it
					searchMenuItem.expandActionView()
					(filterMenuItem?.actionView as Spinner).setSelection(state.filterState.ordinal)
					lblSearch?.text = state.queryText
				}, searchToolbarAnimationDuration)
			}
		}
	}

	@SuppressLint("PrivateResource")
	fun animateSearchToolbar(numberOfMenuIcon: Int, containsOverflow: Boolean, show: Boolean) {

		binding.tbMain.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))

		if (show) {
			binding.msvMainSearch.visibility = View.VISIBLE
			binding.msvMainSearch.viewState = VIEW_STATE_CONTENT
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				val width = binding.tbMain.width -
						if (containsOverflow)
							resources.getDimensionPixelSize(
									R.dimen.abc_action_button_min_width_overflow_material
							)
						else
							(0) - ((resources.getDimensionPixelSize(
									R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2)
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
				val translateAnimation = TranslateAnimation(
						0.0f,
						0.0f,
						((-binding.tbMain.height).toFloat()),
						0.0f
				)
				translateAnimation.duration = searchToolbarAnimationDuration
				binding.tbMain.clearAnimation()
				binding.tbMain.startAnimation(translateAnimation)
			}
		} else {
			binding.msvMainSearch.visibility = View.GONE
			binding.fabMainSearch.show()
			viewModel.clearSearch()
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
						binding.tbMain.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
						activity?.invalidateOptionsMenu()
					}
				})
				createCircularReveal.start()
			} else {
				val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
				val translateAnimation = TranslateAnimation(0.0f, 0.0f, 0.0f, -binding.tbMain.height.toFloat())
				val animationSet = AnimationSet(true)
				animationSet.addAnimation(alphaAnimation)
				animationSet.addAnimation(translateAnimation)
				animationSet.duration = searchToolbarAnimationDuration
				animationSet.setAnimationListener(object : Animation.AnimationListener {
					@Suppress("EmptyFunctionBlock")
					override fun onAnimationStart(animation: Animation) {

					}

					override fun onAnimationEnd(animation: Animation) {
						binding.tbMain.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
						activity?.invalidateOptionsMenu()
					}

					@Suppress("EmptyFunctionBlock")
					override fun onAnimationRepeat(animation: Animation) {

					}
				})
				binding.tbMain.startAnimation(animationSet)
			}
		}
	}

	@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private fun isRtl(resources: Resources): Boolean {
		return resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
	}

	//endregion

	//region render

	@Suppress("EmptyFunctionBlock")
	override fun invalidate() {
	}

	private fun selectSubscribes() {
		viewModel.selectSubscribe(CompaniesState::searchRequest) { searchRequest ->
			val tvMsvSearchError = binding.msvMainSearch.findViewById<TextView>(R.id.tvMsvError)
			when (searchRequest) {
				is Loading -> {
					binding.msvMainSearch.viewState = VIEW_STATE_LOADING
					observeActions()
				}
				is Fail -> {
					binding.msvMainSearch.viewState = VIEW_STATE_ERROR
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
			val tvMsvSearchHistoryError = binding.msvMainSearchHistory.findViewById<TextView>(R.id.tvMsvError)
			val tvMsvSearchHistoryEmpty = binding.msvMainSearchHistory.findViewById<TextView>(R.id.tvMsvEmpty)
			withState(viewModel) { state ->
				when (searchHistoryRequest) {
					is Success -> {
						if (state.searchHistoryVisitables.isNullOrEmpty()) {
							binding.msvMainSearchHistory.viewState = VIEW_STATE_EMPTY
							tvMsvSearchHistoryEmpty.text = getString(R.string.no_recent_searches)
							binding.fabMainSearch.hide()
						} else {
							binding.fabMainSearch.show()
							state.searchHistoryVisitables.let {
								binding.msvMainSearchHistory.viewState = VIEW_STATE_CONTENT
								if (binding.rvMainSearchHistory.adapter == null) {
									searchHistoryAdapter = SearchHistoryAdapter(it, SearchHistoryTypeFactory())
									binding.rvMainSearchHistory.adapter = searchHistoryAdapter
								} else {
									searchHistoryAdapter?.updateItems(it)
								}
								observeActions()
							}
						}
					}
					is Fail -> {
						binding.msvMainSearch.viewState = VIEW_STATE_ERROR
						tvMsvSearchHistoryError.text = searchHistoryRequest.error.message
					}
				}
			}
		}
	}

	private fun showFilteredSearch() {
		val tvMsvSearchEmpty = binding.msvMainSearch.findViewById<TextView>(R.id.tvMsvEmpty)
		binding.fabMainSearch.hide()
		withState(viewModel) { state ->
			if (state.queryText.length >= SEARCH_QUERY_MIN_LENGTH && state.filteredSearchVisitables.isEmpty()) {
				binding.msvMainSearch.viewState = VIEW_STATE_EMPTY
				tvMsvSearchEmpty.text = getString(R.string.no_search_result)
				observeActions()
			} else {
				binding.msvMainSearch.viewState = VIEW_STATE_CONTENT
				if (state.queryText.length > 2) {
					binding.rvMainSearch.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))
					viewModel.logSearch()
				} else
					binding.rvMainSearch.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.semiTransparentBlack))

				state.filteredSearchVisitables.let {
					binding.rvMainSearch.visibility = View.VISIBLE
					binding.msvMainSearch.viewState = VIEW_STATE_CONTENT
					if (binding.rvMainSearch.adapter == null) {
						searchAdapter = SearchAdapter(it, SearchTypeFactory())
						binding.rvMainSearch.adapter = searchAdapter
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

	@Suppress("LongMethod")
	private fun observeActions() {
		eventDisposables.clear()
		lblSearch?.let {
			RxTextView.textChanges(it)
					.skipInitialValue()
					.debounce(
							resources.getInteger(R.integer.search_input_field_debounce).toLong(),
							TimeUnit.MILLISECONDS
					)
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe {
						viewModel.onSearchQueryChanged(lblSearch?.text.toString())
					}
					?.let { queryTextChangeDisposable -> eventDisposables.add(queryTextChangeDisposable) }
		}
		searchHistoryAdapter?.getViewClickedObservable()
				//?.skip(1)
				?.subscribe { view: BaseViewHolder<AbstractSearchHistoryVisitable> ->
					Log.d("logCatText", "observeActions: searchHistoryAdapter clicked ${view.adapterPosition}")
					viewModel.searchHistoryItemClicked(view.adapterPosition)
				}
				?.let { eventDisposables.add(it) }
		searchAdapter?.getViewClickedObservable()
				//?.take(1)
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
					//.skip(1)
					.subscribe {
						viewModel.companiesNavigator.mainToFavourites()
					}
					.also { disposable -> eventDisposables.add(disposable) }
		}
		privacyMenuItem?.let {
			RxMenuItem.clicks(it)
					//.take(1)
					.subscribe {
						viewModel.companiesNavigator.mainToPrivacy()
					}
					.also { disposable -> eventDisposables.add(disposable) }
		}
		binding.fabMainSearch.let {
			RxView.clicks(it)
					//.take(1)
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
