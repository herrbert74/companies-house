package com.babestudios.companyinfouk.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import com.babestudios.base.view.MultiStateView
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.Injector
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.ui.company.createCompanyIntent
import com.babestudios.companyinfouk.ui.favourites.createFavouritesIntent
import com.babestudios.companyinfouk.ui.main.recents.*
import com.babestudios.companyinfouk.ui.main.search.*
import com.babestudios.companyinfouk.ui.privacy.PrivacyActivity
import com.babestudios.companyinfouk.ui.search.SearchPresenter
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
import java.util.concurrent.TimeUnit


class MainActivity : RxAppCompatActivity(), ScopeProvider {


	private var searchHistoryAdapter: SearchHistoryAdapter? = null
	private var searchAdapter: SearchAdapter? = null

	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

	private lateinit var search2Presenter: Search2PresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//Menu
	lateinit var searchMenuItem: MenuItem
	private var favouritesMenuItem: MenuItem? = null
	private var privacyMenuItem: MenuItem? = null
	private var lblSearch: TextView? = null

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(tbMain)
		when {
			viewModel.state.value.searchItems != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				savedInstanceState.getParcelable<SearchState>("STATE")?.let {
					with(viewModel.state.value) {
						searchItems = it.searchItems
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
		if (!::search2Presenter.isInitialized) {
			search2Presenter = Injector.get().searchPresenter()
			search2Presenter.setViewModel(viewModel, requestScope())
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
				search2Presenter.loadMoreSearch(page)
			}
		})
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.search_menu, menu)
		if (viewModel.state.value.contentChange == ContentChange.SEARCH_HISTORY_ITEMS_RECEIVED) {
			menu.removeItem(R.id.spinner)
		}
		//Catch the click on the search button on the soft keyboard and send the query to the presenter
		searchMenuItem = menu.findItem(R.id.action_search)
		favouritesMenuItem = menu.findItem(R.id.action_favourites)
		privacyMenuItem = menu.findItem(R.id.action_privacy)
		val searchView = searchMenuItem.actionView as SearchView
		lblSearch = searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as TextView?
		observeActions()
		lblSearch?.hint = "Search"
		lblSearch?.textColor = ContextCompat.getColor(this, android.R.color.black)
		lblSearch?.setOnEditorActionListener { _, actionId, _ ->
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				rvMainSearch.adapter = null
				val view = this.currentFocus
				if (view != null) {
					view.clearFocus()
					val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
					imm.hideSoftInputFromWindow(view.windowToken, 0)
				}
				return@setOnEditorActionListener true
			}
			false
		}
		searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
			override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
				// Called when SearchView is collapsing
				favouritesMenuItem?.isVisible = true
				privacyMenuItem?.isVisible = true
				searchMenuItem.isVisible = true
				if (searchMenuItem.isActionViewExpanded) {
					animateSearchToolbar(1, containsOverflow = false, show = false)
				}
				return true
			}


			override fun onMenuItemActionExpand(item: MenuItem): Boolean {
				// Called when SearchView is expanding
				//favouritesMenuItem?.isVisible = false
				//privacyMenuItem?.isVisible = false
				animateSearchToolbar(1, containsOverflow = true, show = true)
				return true
			}
		})
		return true
	}

	fun animateSearchToolbar(numberOfMenuIcon: Int, containsOverflow: Boolean, show: Boolean) {

		tbMain.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))

		if (show) {
			msvMainSearch.visibility = View.VISIBLE
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

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			/*R.id.action_TODO -> {
				search2Presenter.TODO
				true
			}*/
			else -> super.onOptionsItemSelected(item)
		}
	}


	//endregion

	//region render

	private fun observeState() {
		viewModel.state
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { render(it) }
	}

	private fun render(state: SearchState) {
		when {
			state.isLoading -> msvMainSearch.viewState = VIEW_STATE_LOADING
			state.errorType != ErrorType.NONE -> msvMainSearch.viewState = VIEW_STATE_ERROR
			state.isSearchLoading -> {
				msvMainSearch.viewState = MultiStateView.VIEW_STATE_LOADING
				observeActions()
			}
			state.contentChange == ContentChange.SEARCH_HISTORY_ITEMS_RECEIVED -> {
				if (state.searchHistoryItems.isNullOrEmpty()) {
					msvMainSearchHistory.viewState = VIEW_STATE_EMPTY
				} else {
					state.searchHistoryItems?.let {
						msvMainSearchHistory.viewState = VIEW_STATE_CONTENT
						if (rvMainSearchHistory?.adapter == null) {
							searchHistoryAdapter = SearchHistoryAdapter(it, SearchHistoryTypeFactory())
							rvMainSearchHistory?.adapter = searchHistoryAdapter
						} else {
							searchHistoryAdapter?.updateItems(it)
						}
						changeFabImage(SearchPresenter.FabImage.FAB_IMAGE_RECENT_SEARCH_DELETE)
						observeActions()
					}
				}
				invalidateOptionsMenu()
			}
			state.contentChange == ContentChange.SEARCH_HISTORY_ITEMS_UPDATED -> {
				state.searchHistoryItems?.let {
					msvMainSearchHistory.viewState = VIEW_STATE_CONTENT
					if (rvMainSearchHistory?.adapter == null) {
						searchHistoryAdapter = SearchHistoryAdapter(it, SearchHistoryTypeFactory())
						rvMainSearchHistory?.adapter = searchHistoryAdapter
					} else {
						searchHistoryAdapter?.updateItems(it)
					}
					changeFabImage(SearchPresenter.FabImage.FAB_IMAGE_RECENT_SEARCH_DELETE)
					observeActions()
				}
			}
			state.contentChange == ContentChange.DELETE_SEARCH_HISTORY -> {
				viewModel.state.value.contentChange = ContentChange.SEARCH_HISTORY_ITEMS_RECEIVED
				showDeleteRecentSearchesDialog()

			}
			state.searchItems == null -> msvMainSearch.viewState = VIEW_STATE_EMPTY
			else -> {
				state.searchItems?.let {
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

	private fun changeFabImage(type: SearchPresenter.FabImage) {
		fabMainSearch?.also {
			it.animate()
					.translationY((6 * resources.getDimensionPixelOffset(R.dimen.fab_size)).toFloat())
					.setStartDelay(100)
					.setInterpolator(AccelerateInterpolator())
					.setDuration(resources.getInteger(R.integer.fab_move_in_duration).toLong())
					.setListener(object : AnimatorListenerAdapter() {
						override fun onAnimationEnd(animation: Animator) {
							if (type === SearchPresenter.FabImage.FAB_IMAGE_RECENT_SEARCH_DELETE) {
								it.setImageResource(R.drawable.ic_delete)
							} else if (type === SearchPresenter.FabImage.FAB_IMAGE_SEARCH_CLOSE) {
								it.setImageResource(R.drawable.ic_close)
							}
							it.animate()
									.translationY(0f)
									.setInterpolator(DecelerateInterpolator())
									.setStartDelay(100)
									.setDuration(resources.getInteger(R.integer.fab_move_in_duration).toLong())
									.start()
						}
					})
					.start()
		}
	}

	fun showDeleteRecentSearchesDialog() {
		AlertDialog.Builder(this)
				.setTitle(R.string.delete_recent_searches)
				.setMessage(R.string.are_you_sure_you_want_to_delete_all_recent_searches)
				.setPositiveButton(android.R.string.yes) { _, _ -> search2Presenter.clearAllRecentSearches() }
				.setNegativeButton(android.R.string.no) { _, _ ->
					// do nothing
				}
				.show()
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
					.subscribe { search2Presenter.onSearchQueryChanged(lblSearch?.text.toString()) }
					?.let { queryTextChangeDisposable -> eventDisposables.add(queryTextChangeDisposable) }
		}
		searchHistoryAdapter?.getViewClickedObservable()
				?.take(1)
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractSearchHistoryVisitable> ->
					viewModel.state.value.searchHistoryItems?.let { searchHistoryItems ->
						val searchHistoryItem = (searchHistoryItems[(view as SearchHistoryViewHolder).adapterPosition] as SearchHistoryVisitable).searchHistoryItem
						startActivityWithRightSlide(this.createCompanyIntent(searchHistoryItem.companyNumber, searchHistoryItem.companyName))
					}
				}
				?.let { eventDisposables.add(it) }
		searchAdapter?.getViewClickedObservable()
				?.take(1)
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractSearchVisitable> ->
					viewModel.state.value.searchItems?.let { searchItems ->
						val searchItem = (searchItems[(view as SearchViewHolder).adapterPosition] as SearchVisitable).searchItem
						(searchItem.companyNumber to searchItem.title).biLet { number, title ->
							search2Presenter.searchItemClicked(number, title)
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
						search2Presenter.fabMainClicked()
					}
					.also { disposable -> eventDisposables.add(disposable) }
		}
	}

	//endregion
}