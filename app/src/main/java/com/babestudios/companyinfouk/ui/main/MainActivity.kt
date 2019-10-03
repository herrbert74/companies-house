package com.babestudios.companyinfouk.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.babestudios.base.ext.biLet
import com.babestudios.base.ext.textColor
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.Injector
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.ext.logScreenView
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.ui.company.createCompanyIntent
import com.babestudios.companyinfouk.ui.favourites.createFavouritesIntent
import com.babestudios.companyinfouk.ui.main.recents.*
import com.babestudios.companyinfouk.ui.main.search.*
import com.babestudios.companyinfouk.ui.privacy.PrivacyActivity
import com.babestudios.base.view.FilterAdapter
import com.google.firebase.analytics.FirebaseAnalytics
import com.jakewharton.rxbinding2.view.RxMenuItem
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.multi_state_view_empty.view.*
import kotlinx.android.synthetic.main.multi_state_view_error.view.*
import java.util.concurrent.TimeUnit


class MainActivity : RxAppCompatActivity(), ScopeProvider {


	private var searchHistoryAdapter: SearchHistoryAdapter? = null
	private var searchAdapter: SearchAdapter? = null

	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

	lateinit var mainPresenter: MainPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var isSearchFromSaved = false
	//Menu
	lateinit var searchMenuItem: MenuItem
	private var favouritesMenuItem: MenuItem? = null
	private var privacyMenuItem: MenuItem? = null
	private var filterMenuItem: MenuItem? = null
	private var lblSearch: TextView? = null

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		logScreenView(this.localClassName)
		setSupportActionBar(tbMain)
		when {
			viewModel.state.value.searchVisitables.isNotEmpty() -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				savedInstanceState.getParcelable<MainState>("STATE")?.let {
					with(viewModel.state.value) {
						searchVisitables = it.searchVisitables
						filterState = it.filterState
						queryText = it.queryText
						totalCount = it.totalCount
						filteredSearchVisitables = it.filteredSearchVisitables
					}
				}
				initPresenter(viewModel)
			}
			else -> {

				initPresenter(viewModel)
			}
		}

		createSearchHistoryRecyclerView()
		createRecyclerView()
		observeState()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putParcelable("STATE", viewModel.state.value)
		super.onSaveInstanceState(outState)
	}

	private fun initPresenter(viewModel: MainViewModel) {
		if (!::mainPresenter.isInitialized) {
			mainPresenter = Injector.get().mainPresenter()
			mainPresenter.setViewModel(viewModel, requestScope())
		}
	}

	private fun createSearchHistoryRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		rvMainSearchHistory.layoutManager = linearLayoutManager
		val titlePositions = java.util.ArrayList<Int>()
		titlePositions.add(0)
		rvMainSearchHistory.addItemDecoration(DividerItemDecorationWithSubHeading(this, titlePositions))
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		rvMainSearch?.layoutManager = linearLayoutManager
		rvMainSearch.addItemDecoration(DividerItemDecoration(this))
		rvMainSearch.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				mainPresenter.loadMoreSearch(page)
			}
		})
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.search_menu, menu)
		//Filter
		filterMenuItem = menu.findItem(R.id.action_filter)
		filterMenuItem?.isVisible = false
		val spinner = filterMenuItem?.actionView as Spinner
		spinner.setBackgroundResource(0)
		spinner.setPadding(0, 0, resources.getDimensionPixelOffset(R.dimen.view_margin), 0)
		val adapter = FilterAdapter(
				this@MainActivity,
				resources.getStringArray(R.array.search_filter_options),
				isDropdownDarkTheme = true,
				isToolbarDarkTheme = false
		)
		spinner.adapter = adapter
		spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
				mainPresenter.setFilterState(FilterState.values()[position])
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
		lblSearch?.textColor = ContextCompat.getColor(this, android.R.color.black)
		searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
			override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
				if (searchMenuItem.isActionViewExpanded) {
					animateSearchToolbar(1, containsOverflow = false, show = false)
				}
				filterMenuItem?.isVisible = false
				searchMenuItem.isVisible = true
				privacyMenuItem?.isVisible = true
				favouritesMenuItem?.isVisible = true
				return true
			}


			override fun onMenuItemActionExpand(item: MenuItem): Boolean {
				animateSearchToolbar(1, containsOverflow = true, show = true)
				favouritesMenuItem?.isVisible = false
				privacyMenuItem?.isVisible = false
				filterMenuItem?.isVisible = true
				return true
			}
		})
		if (isSearchFromSaved) {
			searchMenuItem.expandActionView()
			(searchMenuItem.actionView as SearchView).setQuery(viewModel.state.value.queryText, false)
			(filterMenuItem?.actionView as Spinner).setSelection(viewModel.state.value.filterState.ordinal)
			isSearchFromSaved = false
		}
		return true
	}

	@SuppressLint("PrivateResource")
	fun animateSearchToolbar(numberOfMenuIcon: Int, containsOverflow: Boolean, show: Boolean) {

		tbMain.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))

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
			viewModel.state.value.queryText = ""
			viewModel.state.value.searchVisitables = ArrayList()
			viewModel.state.value.filteredSearchVisitables = ArrayList()
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
						tbMain.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
						invalidateOptionsMenu()
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
						tbMain.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
						invalidateOptionsMenu()
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

	private fun observeState() {
		viewModel.state
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { render(it) }
	}

	private fun render(state: MainState) {
		when {
			state.isLoading -> msvMainSearch.viewState = VIEW_STATE_LOADING
			state.errorType != ErrorType.NONE -> {
				msvMainSearch.viewState = VIEW_STATE_ERROR
				state.errorType = ErrorType.NONE
				msvMainSearch.tvMsvError.text = state.errorMessage
			}
			state.isSearchLoading -> {
				msvMainSearch.viewState = VIEW_STATE_LOADING
				observeActions()
			}
			state.contentChange == ContentChange.SEARCH_HISTORY_ITEMS_RECEIVED -> {
				if (state.searchHistoryVisitables.isNullOrEmpty()) {
					msvMainSearchHistory.viewState = VIEW_STATE_EMPTY
					msvMainSearchHistory.tvMsvEmpty.text = getString(R.string.no_recent_searches)
					fabMainSearch.hide()
				} else {
					fabMainSearch.show()
					state.searchHistoryVisitables?.let {
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
				invalidateOptionsMenu()
			}
			state.contentChange == ContentChange.SEARCH_HISTORY_ITEMS_UPDATED -> {
				state.searchHistoryVisitables?.let {
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
			state.contentChange == ContentChange.DELETE_SEARCH_HISTORY -> {
				viewModel.state.value.contentChange = ContentChange.SEARCH_HISTORY_ITEMS_RECEIVED
				showDeleteRecentSearchesDialog()
			}
			else -> {
				if (state.contentChange == ContentChange.SEARCH_ITEMS_RECEIVED_FROM_SAVED_INSTANCE_STATE) {
					msvMainSearch.visibility = View.VISIBLE
					msvMainSearch.viewState = VIEW_STATE_CONTENT
					isSearchFromSaved = true
				}
				fabMainSearch.hide()
				if (state.queryText.length >= 3 && state.filteredSearchVisitables.isEmpty()) {
					msvMainSearch.viewState = VIEW_STATE_EMPTY
					msvMainSearch.tvMsvEmpty.text = getString(R.string.no_search_result)
					observeActions()
				} else {
					msvMainSearch.viewState = VIEW_STATE_CONTENT
					if (viewModel.state.value.queryText.let { it.length > 2 }) {
						rvMainSearch.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
						logSearch()
					} else
						rvMainSearch.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent_black))

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
	}

	private fun showDeleteRecentSearchesDialog() {
		AlertDialog.Builder(this)
				.setTitle(R.string.delete_recent_searches)
				.setMessage(R.string.are_you_sure_you_want_to_delete_all_recent_searches)
				.setPositiveButton(android.R.string.yes) { _, _ -> mainPresenter.clearAllRecentSearches() }
				.setNegativeButton(android.R.string.no) { _, _ ->
					// do nothing
				}
				.show()
		observeActions()
	}

	private fun logSearch() {
		val bundle = Bundle()
		bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, viewModel.state.value.queryText)
		CompaniesHouseApplication.instance.firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
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
					.`as`(AutoDispose.autoDisposable(this))
					.subscribe { mainPresenter.onSearchQueryChanged(lblSearch?.text.toString()) }
					?.let { queryTextChangeDisposable -> eventDisposables.add(queryTextChangeDisposable) }
		}
		searchHistoryAdapter?.getViewClickedObservable()
				?.take(1)
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractSearchHistoryVisitable> ->
					viewModel.state.value.searchHistoryVisitables?.let { searchHistoryItems ->
						val searchHistoryItem = (searchHistoryItems[(view as SearchHistoryViewHolder).adapterPosition] as SearchHistoryVisitable).searchHistoryItem
						startActivityWithRightSlide(this.createCompanyIntent(searchHistoryItem.companyNumber, searchHistoryItem.companyName))
					}
				}
				?.let { eventDisposables.add(it) }
		searchAdapter?.getViewClickedObservable()
				?.take(1)
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractSearchVisitable> ->
					viewModel.state.value.filteredSearchVisitables.let { searchItems ->
						val searchItem = (searchItems[(view as SearchViewHolder).adapterPosition] as SearchVisitable).searchItem
						(searchItem.title to searchItem.companyNumber).biLet { title, number ->
							mainPresenter.searchItemClicked(title, number)
							startActivityWithRightSlide(this.createCompanyIntent(number, title))
						}
					}
				}
				?.let { eventDisposables.add(it) }
		favouritesMenuItem?.let {
			RxMenuItem.clicks(it)
					.take(1)
					.`as`(AutoDispose.autoDisposable(this))
					.subscribe { startActivityWithRightSlide(createFavouritesIntent()) }
					.also { disposable -> eventDisposables.add(disposable) }
		}
		privacyMenuItem?.let {
			RxMenuItem.clicks(it)
					.take(1)
					.`as`(AutoDispose.autoDisposable(this))
					.subscribe {
						intent = Intent(this, PrivacyActivity::class.java)
						startActivityWithRightSlide(intent)
					}
					.also { disposable -> eventDisposables.add(disposable) }
		}
		fabMainSearch?.let {
			RxView.clicks(it)
					.take(1)
					.`as`(AutoDispose.autoDisposable(this))
					.subscribe {
						mainPresenter.fabMainClicked()
					}
					.also { disposable -> eventDisposables.add(disposable) }
		}
	}

	//endregion
}