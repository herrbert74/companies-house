package com.babestudios.companyinfouk.filings.ui.filinghistory

import android.os.Bundle
import android.view.*
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.FilterAdapter
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.common.model.filinghistory.CategoryDto
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.filings.ui.FilingsViewModel
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryAdapter
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryTypeFactory
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryViewHolder
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryVisitable
import com.jakewharton.rxbinding2.widget.RxAdapterView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_filing_history.*

class FilingHistoryFragment : BaseMvRxFragment() {

	private val viewModel by activityViewModel(FilingsViewModel::class)

	private var filingHistoryAdapter: FilingHistoryAdapter? = null

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private lateinit var spinner: Spinner

	//region lifeCycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {

		return inflater.inflate(R.layout.fragment_filing_history, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(pabFilingHistory.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		pabFilingHistory.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.setTitle(R.string.filing_history)
		createFilingHistoryRecyclerView()
		viewModel.getFilingHistory()
	}

	private fun createFilingHistoryRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		rvFilingHistory?.layoutManager = linearLayoutManager
		rvFilingHistory?.addItemDecoration(
				DividerItemDecoration(requireContext()))

		rvFilingHistory?.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				viewModel.loadMoreFilingHistory(page)
			}
		})
	}

	override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
		menuInflater.inflate(R.menu.filing_history_menu, menu)
		val item = menu.findItem(R.id.action_filter)
		spinner = item.actionView as Spinner
		spinner.setBackgroundResource(0)
		spinner.setPadding(0, 0, resources.getDimensionPixelOffset(R.dimen.view_margin), 0)
		spinner.gravity = Gravity.END
		val adapter = FilterAdapter(
				requireContext(),
				resources.getStringArray(R.array.filing_history_categories),
				isDropdownDarkTheme = true,
				isToolbarDarkTheme = true
		)
		spinner.adapter = adapter
		withState(viewModel) { state ->
			if (state.filingCategoryFilter != CategoryDto.CATEGORY_SHOW_ALL) {
				spinner.setSelection(state.filingCategoryFilter.ordinal)
			}
		}
	}

	//endregion

	//region Actions

	private fun observeActions() {
		eventDisposables.clear()
		if (::spinner.isInitialized) {
			RxAdapterView.itemSelections(spinner)
					.skip(1)
					.subscribe { er -> viewModel.setCategoryFilter(er) }
					?.let { eventDisposables.add(it) }
		}
		filingHistoryAdapter?.getViewClickedObservable()
				?.subscribe { view: BaseViewHolder<FilingHistoryVisitable> ->
					viewModel.filingHistoryItemClicked((view as FilingHistoryViewHolder).adapterPosition)
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion

	//region Render

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.filingsRequest) {
				is Success -> {
					if(state.filingsHistory.isEmpty()) {
						msvFilingHistory.viewState = VIEW_STATE_EMPTY
					} else {
						msvFilingHistory.viewState = VIEW_STATE_CONTENT
						showFilingHistory()
						observeActions()
					}
				}
				is Loading -> {
					msvFilingHistory.viewState = VIEW_STATE_LOADING
				}
				is Fail -> {
					msvFilingHistory.viewState = VIEW_STATE_ERROR
					val tvMsvError = msvFilingHistory.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.filingsRequest.error.message
				}
			}
		}
	}

	private fun showFilingHistory() {
		msvFilingHistory.viewState = VIEW_STATE_CONTENT
		withState(viewModel) { state ->
			if (rvFilingHistory?.adapter == null) {
				filingHistoryAdapter = FilingHistoryAdapter(state.filingsHistory, FilingHistoryTypeFactory())
				rvFilingHistory?.adapter = filingHistoryAdapter
			} else {
				filingHistoryAdapter?.updateItems(state.filingsHistory)
			}
		}
	}

	//endregion

}
