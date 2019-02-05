package com.babestudios.companyinfouk.ui.search

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.ui.company.createCompanyIntent
import com.babestudios.companyinfouk.ui.favourites.FavouritesActivity
import com.babestudios.companyinfouk.ui.privacy.PrivacyActivity
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.google.firebase.analytics.FirebaseAnalytics
import com.pascalwelsch.compositeandroid.activity.CompositeActivity
import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin
import java.util.*
import javax.inject.Inject

class SearchActivity : CompositeActivity(), SearchActivityView, SearchResultsAdapter.SearchResultsRecyclerViewClickListener, RecentSearchesResultsAdapter.RecentSearchesRecyclerViewClickListener {

	@JvmField
	@BindView(R.id.toolbar)
	internal var toolbar: Toolbar? = null

	@JvmField
	@BindView(R.id.recent_searches_recycler_view)
	internal var recentSearchesRecyclerView: RecyclerView? = null

	@JvmField
	@BindView(R.id.search_recycler_view)
	internal var searchRecyclerView: RecyclerView? = null

	@JvmField
	@BindView(R.id.fab)
	internal var fab: FloatingActionButton? = null

	private var searchResultsAdapter: SearchResultsAdapter? = null

	private var recentSearchesResultsAdapter: RecentSearchesResultsAdapter? = null

	@JvmField
	@BindView(R.id.progressbar)
	internal var progressbar: ProgressBar? = null

	private var searchMenuItem: MenuItem? = null

	@Inject
	internal lateinit var searchPresenter: SearchPresenter

	private var initialFilterState: SearchPresenter.FilterState? = null

	internal var searchActivityPlugin = TiActivityPlugin<SearchPresenter, SearchActivityView> {
		CompaniesHouseApplication.instance.applicationComponent.inject(this@SearchActivity)
		searchPresenter
	}

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {
		addPlugin(searchActivityPlugin)
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_search)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar?.setDisplayHomeAsUpEnabled(false)
			toolbar?.setNavigationOnClickListener { onBackPressed() }
		}
		createRecentSearchesRecyclerView()
		createSearchResultsRecyclerView()
		fab?.setOnClickListener { searchActivityPlugin.presenter.onFabClicked() }
		performAnimations()
	}

	private fun performAnimations() {
		fab?.let {
			it.translationY = (2 * resources.getDimensionPixelOffset(R.dimen.fab_size)).toFloat()
			it.animate()
					.translationY(0f)
					.setInterpolator(DecelerateInterpolator())
					.setStartDelay(resources.getInteger(R.integer.fab_move_in_start_delay).toLong())
					.setDuration(resources.getInteger(R.integer.fab_move_in_duration).toLong())
					.start()
		}
	}

	private fun createRecentSearchesRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		recentSearchesRecyclerView?.layoutManager = linearLayoutManager
		val titlePositions = ArrayList<Int>()
		titlePositions.add(0)
		recentSearchesRecyclerView?.addItemDecoration(
				DividerItemDecorationWithSubHeading(this, titlePositions))
	}

	private fun createSearchResultsRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		searchRecyclerView?.layoutManager = linearLayoutManager
		searchRecyclerView?.addItemDecoration(
				DividerItemDecoration(this))

		searchRecyclerView?.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				searchActivityPlugin.presenter.searchLoadMore(page)
			}
		})
	}

	override fun showProgress() {
		progressbar?.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progressbar?.visibility = View.GONE
	}

	override fun showRecentSearches(searchHistoryItems: ArrayList<SearchHistoryItem>) {
		recentSearchesRecyclerView?.visibility = View.VISIBLE
		searchRecyclerView?.visibility = View.GONE
		if (recentSearchesRecyclerView?.adapter == null) {
			recentSearchesResultsAdapter = RecentSearchesResultsAdapter(this@SearchActivity, searchHistoryItems)
			recentSearchesRecyclerView?.adapter = recentSearchesResultsAdapter
		}
	}

	override fun refreshRecentSearchesAdapter(searchHistoryItems: ArrayList<SearchHistoryItem>) {
		if (recentSearchesRecyclerView?.adapter == null) {
			recentSearchesResultsAdapter = RecentSearchesResultsAdapter(this@SearchActivity, searchHistoryItems)
			recentSearchesRecyclerView?.adapter = recentSearchesResultsAdapter
		} else {
			recentSearchesResultsAdapter?.refreshData(searchHistoryItems)
		}
	}

	override fun startCompanyActivity(companyNumber: String, companyName: String) {
		baseActivityPlugin.startActivityWithRightSlide(createCompanyIntent(companyNumber, companyName))
	}

	/**
	 * @param companySearchResult
	 * @param isFromOnNext:       addItems on adapter should only be used for onLoadMore from onNext
	 * @param filterState
	 */
	override fun showCompanySearchResult(companySearchResult: CompanySearchResult, isFromOnNext: Boolean, filterState: SearchPresenter.FilterState) {
		recentSearchesRecyclerView?.visibility = View.GONE
		searchRecyclerView?.visibility = View.VISIBLE
		if (searchRecyclerView?.adapter == null) {
			searchResultsAdapter = SearchResultsAdapter(this@SearchActivity, companySearchResult, filterState)
			searchRecyclerView?.adapter = searchResultsAdapter
		} else if (isFromOnNext) {
			searchResultsAdapter?.addItems(companySearchResult, filterState)
		}
	}

	override fun clearSearchView() {
		searchMenuItem?.collapseActionView()
	}

	override fun changeFabImage(type: SearchPresenter.FabImage) {
		fab?.also {
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

	override fun showDeleteRecentSearchesDialog() {
		AlertDialog.Builder(this)
				.setTitle(R.string.delete_recent_searches)
				.setMessage(R.string.are_you_sure_you_want_to_delete_all_recent_searches)
				.setPositiveButton(android.R.string.yes) { _, _ -> searchActivityPlugin.presenter.clearAllRecentSearches() }
				.setNegativeButton(android.R.string.no) { _, _ ->
					// do nothing
				}
				.show()
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.search_menu, menu)

		//Catch the click on the search button on the soft keyboard and send the query to the presenter
		searchMenuItem = menu.findItem(R.id.action_search)
		val searchView = searchMenuItem?.actionView as SearchView
		val searchText = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_src_text) as TextView
		searchText.setOnEditorActionListener { _, actionId, _ ->
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				searchRecyclerView?.adapter = null
				val view = this.currentFocus
				if (view != null) {
					view.clearFocus()
					val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
					imm.hideSoftInputFromWindow(view.windowToken, 0)
				}

				val queryText = searchView.query.toString()
				logSearch(queryText)
				searchActivityPlugin.presenter.search(queryText)
				return@setOnEditorActionListener true
			}
			false
		}
		val item = menu.findItem(R.id.spinner)
		val spinner = item.actionView as Spinner
		spinner.setBackgroundResource(0)
		spinner.setPadding(0, 0, resources.getDimensionPixelOffset(R.dimen.view_margin), 0)
		val adapter = SearchFilterAdapter(this@SearchActivity, resources.getStringArray(R.array.search_filter_options), true)
		spinner.adapter = adapter
		initialFilterState?.let {
			spinner.setSelection(it.ordinal)
			initialFilterState = null
		}
		spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
				searchActivityPlugin.presenter.setFilterState(position)
			}

			override fun onNothingSelected(parent: AdapterView<*>) {

			}
		}
		return true
	}

	private fun logSearch(queryText: String) {
		val bundle = Bundle()
		bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, queryText)
		CompaniesHouseApplication.instance.firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
	}

	override fun setInitialFilterState(filterState: SearchPresenter.FilterState) {
		this.initialFilterState = filterState
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val intent: Intent
		return when (item.itemId) {
			R.id.action_favourites -> {
				intent = Intent(this, FavouritesActivity::class.java)
				baseActivityPlugin.startActivityWithRightSlide(intent)
				true
			}
			R.id.action_privacy -> {
				intent = Intent(this, PrivacyActivity::class.java)
				baseActivityPlugin.startActivityWithRightSlide(intent)
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun searchResultItemClicked(v: View, position: Int, companyName: String, companyNumber: String) {
		searchActivityPlugin.presenter.getCompany(companyName, companyNumber)
	}

	override fun recentSearchesResultItemClicked(v: View, position: Int, companyName: String, companyNumber: String) {
		searchActivityPlugin.presenter.getCompany(companyName, companyNumber)
	}

	override fun setFilterOnAdapter(filterState: SearchPresenter.FilterState) {
		searchResultsAdapter?.setFilterOnAdapter(filterState)
	}

	override fun showError() {
		Toast.makeText(this, R.string.could_not_complete_search, Toast.LENGTH_LONG).show()
	}
}
