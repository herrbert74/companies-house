package com.babestudios.companyinfouk.ui.filinghistory

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.widget.Spinner
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.Injector
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.filinghistory.Category
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.ext.logScreenView
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.ui.filinghistorydetails.FilingHistoryDetailsActivity
import com.babestudios.companyinfouk.ui.search.SearchFilterAdapter
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.google.gson.Gson
import com.jakewharton.rxbinding2.widget.RxAdapterView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import kotlinx.android.synthetic.main.activity_filing_history.*

class FilingHistoryActivity : RxAppCompatActivity(), ScopeProvider {

	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(FilingHistoryViewModel::class.java) }

	private var filingHistoryAdapter: FilingHistoryAdapter? = null

	lateinit var filingHistoryPresenter: FilingHistoryPresenterContract

	//region lifeCycle

	private lateinit var spinner: Spinner

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_filing_history)
		logScreenView(this.localClassName)

		setSupportActionBar(tbFilingHistory)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		tbFilingHistory?.setNavigationOnClickListener { onBackPressed() }
		val companyNumber = intent.getStringExtra("companyNumber")
		createFilingHistoryRecyclerView()

		ctbFilingHistory?.title = getString(R.string.filing_history)
		initPresenter(companyNumber)
		observeState()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	private fun initPresenter(companyNumber: String) {
		val maybePresenter = lastCustomNonConfigurationInstance as FilingHistoryPresenterContract?

		if (maybePresenter != null) {
			filingHistoryPresenter = maybePresenter
		}

		if (!::filingHistoryPresenter.isInitialized) {
			viewModel.state.value.companyNumber = companyNumber
			filingHistoryPresenter = Injector.get().filingHistoryPresenter()
			filingHistoryPresenter.setViewModel(viewModel, requestScope())
		}
	}

	override fun onRetainCustomNonConfigurationInstance() =
			filingHistoryPresenter

	private fun createFilingHistoryRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		rvFilingHistory?.layoutManager = linearLayoutManager
		rvFilingHistory?.addItemDecoration(
				DividerItemDecoration(this))

		rvFilingHistory?.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				filingHistoryPresenter.loadMoreFilingHistory(page)
			}
		})
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.filing_history_menu, menu)

		val item = menu.findItem(R.id.spinner)
		spinner = item.actionView as Spinner
		spinner.setBackgroundResource(0)
		spinner.setPadding(0, 0, resources.getDimensionPixelOffset(R.dimen.view_margin), 0)
		val adapter = SearchFilterAdapter(this@FilingHistoryActivity, resources.getStringArray(R.array.filing_history_categories), true)
		spinner.adapter = adapter
		if (viewModel.state.value.filingCategoryFilter != Category.CATEGORY_SHOW_ALL) {
			spinner.setSelection(viewModel.state.value.filingCategoryFilter.ordinal)
		}
		observeActions()
		return true
	}

	//endregion

	//region Actions

	private fun observeActions() {
		if (::spinner.isInitialized) {
			RxAdapterView.itemSelections(spinner)
					.skip(2)
					.`as`(AutoDispose.autoDisposable(this))
					.subscribe { er -> filingHistoryPresenter.setCategoryFilter(er) }
			filingHistoryAdapter?.getViewClickedObservable()
					?.`as`(AutoDispose.autoDisposable(this))
					?.subscribe { view: BaseViewHolder<FilingHistoryVisitable> ->
						filingItemClicked(viewModel.state.value.filingHistoryList[(view as FilingHistoryViewHolder).adapterPosition].filingHistoryItem)
					}
		}
	}

	private fun filingItemClicked(item: FilingHistoryItem?) {
		val gson = Gson()
		val jsonItem = gson.toJson(item, FilingHistoryItem::class.java)
		val intent = Intent(this, FilingHistoryDetailsActivity::class.java)
		intent.putExtra("filingHistoryItem", jsonItem)
		startActivityWithRightSlide(intent)
	}

	//endregion

	//region Render

	private fun observeState() {
		viewModel.state
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { render(it) }
	}

	private fun render(state: FilingHistoryState) {
		when {
			state.contentChange == ContentChange.SHOW_FILING_HISTORY_ITEM -> {
				state.contentChange = ContentChange.NONE
				filingItemClicked(state.clickedFilingHistoryItem)
			}
			state.isLoading -> msvFilingHistory.viewState = VIEW_STATE_LOADING
			state.errorType != ErrorType.NONE -> msvFilingHistory.viewState = VIEW_STATE_ERROR
			state.filingHistoryList.isEmpty() -> {
				msvFilingHistory.viewState = VIEW_STATE_EMPTY
			}
			else -> {
				msvFilingHistory.viewState = VIEW_STATE_CONTENT
				showFilingHistory()
				observeActions()
			}
		}
	}

	private fun showFilingHistory() {
		msvFilingHistory.viewState = VIEW_STATE_CONTENT
		if (rvFilingHistory?.adapter == null) {
			filingHistoryAdapter = FilingHistoryAdapter(viewModel.state.value.filingHistoryList, FilingHistoryTypesFactory())
			rvFilingHistory?.adapter = filingHistoryAdapter
		} else {
			filingHistoryAdapter?.updateItems(viewModel.state.value.filingHistoryList)
		}
	}

	override fun onBackPressed() {
		super.finish()
		overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}

	//endregion
}
