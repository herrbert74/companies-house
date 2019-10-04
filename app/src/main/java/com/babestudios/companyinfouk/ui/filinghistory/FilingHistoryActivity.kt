package com.babestudios.companyinfouk.ui.filinghistory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.widget.Spinner
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.FilterAdapter
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfo.core.injection.CoreInjectHelper
import com.babestudios.companyinfo.data.model.filinghistory.Category
import com.babestudios.companyinfo.data.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.ext.logScreenView
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.ui.filinghistory.list.FilingHistoryAdapter
import com.babestudios.companyinfouk.ui.filinghistory.list.FilingHistoryTypeFactory
import com.babestudios.companyinfouk.ui.filinghistory.list.FilingHistoryViewHolder
import com.babestudios.companyinfouk.ui.filinghistory.list.FilingHistoryVisitable
import com.babestudios.companyinfouk.ui.filinghistorydetails.createFilingHistoryDetailsIntent
import com.jakewharton.rxbinding2.widget.RxAdapterView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_filing_history.*
import kotlinx.android.synthetic.main.multi_state_view_error.view.*

private const val COMPANY_NUMBER = "com.babestudios.companyinfouk.ui.company_number"

class FilingHistoryActivity : RxAppCompatActivity(), ScopeProvider {

	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(FilingHistoryViewModel::class.java) }

	private var filingHistoryAdapter: FilingHistoryAdapter? = null

	lateinit var filingHistoryPresenter: FilingHistoryPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private lateinit var comp: FilingHistoryComponent

	private lateinit var spinner: Spinner

	//region lifeCycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_filing_history)
		logScreenView(this.localClassName)

		setSupportActionBar(pabFilingHistory.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabFilingHistory?.setNavigationOnClickListener { onBackPressed() }
		createFilingHistoryRecyclerView()

		supportActionBar?.title = getString(R.string.filing_history)
		when {
			viewModel.state.value.filingHistoryList != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				savedInstanceState.getParcelable<FilingHistoryState>("STATE")?.let {
					with(viewModel.state.value) {
						companyNumber = it.companyNumber
						filingHistoryList = it.filingHistoryList
						filingCategoryFilter = it.filingCategoryFilter
						clickedFilingHistoryItem = null
					}
				}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value.companyNumber = intent.getStringExtra(COMPANY_NUMBER)
				initPresenter(viewModel)
			}
		}
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

	private fun initPresenter(viewModel: FilingHistoryViewModel) {
		val maybePresenter = lastCustomNonConfigurationInstance as FilingHistoryPresenterContract?

		if (maybePresenter != null) {
			filingHistoryPresenter = maybePresenter
		}

		if (!::filingHistoryPresenter.isInitialized) {
			comp = DaggerFilingHistoryComponent
					.builder()
					.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
					.build()
			filingHistoryPresenter = comp.filingHistoryPresenter()
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

		val item = menu.findItem(R.id.action_filter)
		spinner = item.actionView as Spinner
		spinner.setBackgroundResource(0)
		spinner.setPadding(0, 0, resources.getDimensionPixelOffset(R.dimen.view_margin), 0)
		spinner.gravity = Gravity.END
		val adapter = FilterAdapter(
				this@FilingHistoryActivity,
				resources.getStringArray(R.array.filing_history_categories),
				isDropdownDarkTheme = true,
				isToolbarDarkTheme = true
		)
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
		eventDisposables.clear()
		if (::spinner.isInitialized) {
			RxAdapterView.itemSelections(spinner)
					.skip(1)
					.`as`(AutoDispose.autoDisposable(this))
					.subscribe { er -> filingHistoryPresenter.setCategoryFilter(er) }
					?.let { eventDisposables.add(it) }
		}
		filingHistoryAdapter?.getViewClickedObservable()
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<FilingHistoryVisitable> ->
					viewModel.state.value.filingHistoryList?.let { filingHistoryList ->
						filingItemClicked(filingHistoryList[(view as FilingHistoryViewHolder).adapterPosition].filingHistoryItem)
					}
				}
				?.let { eventDisposables.add(it) }
	}

	private fun filingItemClicked(item: FilingHistoryItem?) {
		item?.let {
			val detailsIntent = createFilingHistoryDetailsIntent(it)
			startActivityWithRightSlide(detailsIntent)
		}
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
			state.isLoading -> {
				msvFilingHistory.viewState = VIEW_STATE_LOADING
			}
			state.errorType != ErrorType.NONE -> {
				msvFilingHistory.viewState = VIEW_STATE_ERROR
				state.errorType = ErrorType.NONE
				msvFilingHistory.tvMsvError.text = state.errorMessage
			}
			state.filingHistoryList?.isNotEmpty() == false -> {
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
		viewModel.state.value.filingHistoryList?.also { filingHistoryList ->
			if (rvFilingHistory?.adapter == null) {
				filingHistoryAdapter = FilingHistoryAdapter(filingHistoryList, FilingHistoryTypeFactory())
				rvFilingHistory?.adapter = filingHistoryAdapter
			} else {
				filingHistoryAdapter?.updateItems(filingHistoryList)
			}
		}
	}

	override fun onBackPressed() {
		super.finish()
		overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}

	//endregion
}

fun Context.createFilingHistoryIntent(companyNumber: String): Intent {
	return Intent(this, FilingHistoryActivity::class.java)
			.putExtra(COMPANY_NUMBER, companyNumber)
}
