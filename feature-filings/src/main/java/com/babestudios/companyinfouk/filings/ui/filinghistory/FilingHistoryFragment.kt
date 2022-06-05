package com.babestudios.companyinfouk.filings.ui.filinghistory

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.annotations.MainThread
import com.arkivanov.mvikotlin.core.view.MviView
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.internal.PublishSubject
import com.babestudios.base.network.OfflineException
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.FilterAdapter
import com.babestudios.base.view.MultiStateView.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.VIEW_STATE_EMPTY
import com.babestudios.base.view.MultiStateView.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.common.ext.viewBinding
import com.babestudios.companyinfouk.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.filings.databinding.FragmentFilingHistoryBinding
import com.babestudios.companyinfouk.filings.ui.FilingsViewModel
import com.babestudios.companyinfouk.filings.ui.FilingsViewModelFactory
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryStore.State
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryAdapter
import com.babestudios.companyinfouk.navigation.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.itemSelections

@AndroidEntryPoint
class FilingHistoryFragment : Fragment(R.layout.fragment_filing_history), MviView<State, UserIntent> {

	@Inject
	lateinit var filingsViewModelFactory: FilingsViewModelFactory

	private val args: FilingHistoryFragmentArgs by navArgs()

	private val viewModel: FilingsViewModel by viewModels {
		FilingsViewModel.provideFactory(
			filingsViewModelFactory,
			args.selectedCompanyId
		)
	}

	private var filingHistoryAdapter: FilingHistoryAdapter? = null

	private val binding by viewBinding<FragmentFilingHistoryBinding>()

	private lateinit var spinner: Spinner

	//region BaseMviView This is just a copy from com.arkivanov.mvikotlin.core.view.BaseMviView,
	// as we cannot use it here and do not want to use it separately. This part could be extracted into a delegate

	private val subject = PublishSubject<UserIntent>()

	override fun events(observer: Observer<UserIntent>): Disposable = subject.subscribe(observer)

	fun sideEffects(sideEffect: SideEffect) {
		when (sideEffect) {
			is SideEffect.FilingItemClicked ->
				findNavController().navigateSafe(
					FilingHistoryFragmentDirections.actionToDetails(
						args.selectedCompanyId, sideEffect.selectedFilingHistoryItem
					)
				)
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

		when (model) {
			is State.Loading -> binding.msvFilingHistory.viewState = VIEW_STATE_LOADING
			is State.Error -> {
				if (model.t is OfflineException) {
					binding.msvFilingHistory.viewState = VIEW_STATE_EMPTY
				} else {
					binding.msvFilingHistory.viewState = VIEW_STATE_ERROR
					val tvMsvError = binding.msvFilingHistory.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = model.t.message
				}
			}
			is State.Show -> {
				if (model.filingHistory.items.isEmpty()) {
					binding.msvFilingHistory.viewState = VIEW_STATE_EMPTY
				} else {
					binding.msvFilingHistory.viewState = VIEW_STATE_CONTENT
					if (binding.rvFilingHistory.adapter == null) {
						filingHistoryAdapter = FilingHistoryAdapter(model.filingHistory.items, lifecycleScope)
						binding.rvFilingHistory.adapter = filingHistoryAdapter
						filingHistoryAdapter?.itemClicks?.onEach {
							dispatch(UserIntent.FilingItemClicked(it))
						}?.launchIn(lifecycleScope)
					} else {
						filingHistoryAdapter?.updateItems(model.filingHistory.items)
					}
				}

				if (model.filingCategoryFilter != Category.CATEGORY_SHOW_ALL) {
					spinner.setSelection(model.filingCategoryFilter.ordinal)
				}

				if (::spinner.isInitialized) {
					spinner.itemSelections()
						.drop(1).onEach { ordinal ->
							dispatch(UserIntent.FilingCategorySelected(ordinal))
						}.launchIn(lifecycleScope)
				}
			}
		}
	}

	//endregion

	//region lifeCycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.onViewCreated(this, essentyLifecycle())
		initializeToolBar()
		createFilingHistoryRecyclerView()
	}

	private fun initializeToolBar() {
		(activity as AppCompatActivity).setSupportActionBar(binding.pabFilingHistory.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabFilingHistory.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.setTitle(R.string.filing_history)
	}

	private fun createFilingHistoryRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvFilingHistory.layoutManager = linearLayoutManager
		binding.rvFilingHistory.addItemDecoration(
			DividerItemDecoration(requireContext())
		)

		binding.rvFilingHistory.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				dispatch(UserIntent.LoadMoreFilings(page))
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
	}

	//endregion

}

sealed class UserIntent {
	data class FilingItemClicked(val selectedFilingHistoryItem: FilingHistoryItem) : UserIntent()
	data class LoadMoreFilings(val page: Int) : UserIntent()
	data class FilingCategorySelected(val categoryOrdinal: Int) : UserIntent()
}

sealed class SideEffect {
	data class FilingItemClicked(val selectedFilingHistoryItem: FilingHistoryItem) : SideEffect()
}
