package com.babestudios.companyinfouk.filings.ui.filinghistory

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.base.mvrx.BaseFragment
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.FilterAdapter
import com.babestudios.base.view.MultiStateView.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.VIEW_STATE_EMPTY
import com.babestudios.base.view.MultiStateView.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.filings.databinding.FragmentFilingHistoryBinding
import com.babestudios.companyinfouk.filings.ui.FilingsActivity
import com.babestudios.companyinfouk.filings.ui.FilingsViewModel
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryAdapter
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryTypeFactory
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryViewHolder
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryVisitable
import com.jakewharton.rxbinding2.widget.RxAdapterView
import io.reactivex.disposables.CompositeDisposable

class FilingHistoryFragment : BaseFragment() {

	private val viewModel by activityViewModel(FilingsViewModel::class)

	private var filingHistoryAdapter: FilingHistoryAdapter? = null

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentFilingHistoryBinding? = null
	private val binding get() = _binding!!

	private lateinit var spinner: Spinner

	//region lifeCycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun orientationChanged() {
		val activity = requireActivity() as FilingsActivity
		viewModel.setNavigator(activity.injectFilingsNavigator())
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentFilingHistoryBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabFilingHistory.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabFilingHistory.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.setTitle(R.string.filing_history)
		createFilingHistoryRecyclerView()
		viewModel.getFilingHistory()
	}

	private fun createFilingHistoryRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvFilingHistory.layoutManager = linearLayoutManager
		binding.rvFilingHistory.addItemDecoration(
			DividerItemDecoration(requireContext()))

		binding.rvFilingHistory.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
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
		spinner.setPadding(0, 0, resources.getDimensionPixelOffset(R.dimen.screenMargin), 0)
		spinner.gravity = Gravity.END
		val adapter = FilterAdapter(
			requireContext(),
			resources.getStringArray(R.array.filing_history_categories),
			isDropdownDarkTheme = true,
			isToolbarDarkTheme = true
		)
		spinner.adapter = adapter
		withState(viewModel) { state ->
			if (state.filingCategoryFilter != Category.CATEGORY_SHOW_ALL) {
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
					if (state.filingsHistory.isEmpty()) {
						binding.msvFilingHistory.viewState = VIEW_STATE_EMPTY
					} else {
						binding.msvFilingHistory.viewState = VIEW_STATE_CONTENT
						showFilingHistory()
						observeActions()
					}
				}
				is Loading -> {
					binding.msvFilingHistory.viewState = VIEW_STATE_LOADING
				}
				is Fail -> {
					binding.msvFilingHistory.viewState = VIEW_STATE_ERROR
					val tvMsvError = binding.msvFilingHistory.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.filingsRequest.error.message
				}
				else -> {}
			}
		}
	}

	private fun showFilingHistory() {
		binding.msvFilingHistory.viewState = VIEW_STATE_CONTENT
		withState(viewModel) { state ->
			if (binding.rvFilingHistory.adapter == null) {
				filingHistoryAdapter = FilingHistoryAdapter(state.filingsHistory, FilingHistoryTypeFactory())
				binding.rvFilingHistory.adapter = filingHistoryAdapter
			} else {
				filingHistoryAdapter?.updateItems(state.filingsHistory)
			}
		}
	}

	//endregion

}
