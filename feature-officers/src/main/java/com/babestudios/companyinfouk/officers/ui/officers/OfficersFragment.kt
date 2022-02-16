package com.babestudios.companyinfouk.officers.ui.officers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.base.mvrx.BaseFragment
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.view.MultiStateView.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.VIEW_STATE_EMPTY
import com.babestudios.base.view.MultiStateView.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.officers.R
import com.babestudios.companyinfouk.officers.databinding.FragmentOfficersBinding
import com.babestudios.companyinfouk.officers.ui.OfficersActivity
import com.babestudios.companyinfouk.officers.ui.OfficersViewModel
import com.babestudios.companyinfouk.officers.ui.officers.list.OfficersAdapter
import com.babestudios.companyinfouk.officers.ui.officers.list.OfficersTypeFactory
import com.babestudios.companyinfouk.officers.ui.officers.list.OfficersVisitableBase
import io.reactivex.disposables.CompositeDisposable

class OfficersFragment : BaseFragment() {

	private var officersAdapter: OfficersAdapter? = null

	private val viewModel by activityViewModel(OfficersViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentOfficersBinding? = null
	private val binding get() = _binding!!

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		_binding = FragmentOfficersBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabOfficers.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabOfficers.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.setTitle(R.string.officers)
		createRecyclerView()
		withState(viewModel) {
			viewModel.fetchOfficers(it.companyNumber)
		}
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun orientationChanged() {
		val activity = requireActivity() as OfficersActivity
		viewModel.setNavigator(activity.injectOfficersNavigator())
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
		binding.rvOfficers.layoutManager = linearLayoutManager
		binding.rvOfficers.addItemDecoration(DividerItemDecoration(requireContext()))
		binding.rvOfficers.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				viewModel.loadMoreOfficers(page)
			}
		})
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		officersAdapter?.getViewClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<OfficersVisitableBase> ->
					viewModel.officerItemClicked(view.adapterPosition)
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.officersRequest) {
				is Loading -> binding.msvOfficers.viewState = VIEW_STATE_LOADING
				is Fail -> {
					binding.msvOfficers.viewState = VIEW_STATE_ERROR
					val tvMsvError = binding.msvOfficers.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.officersRequest.error.message
				}
				is Success -> {
					if (state.officerItems.isEmpty()) {
						binding.msvOfficers.viewState = VIEW_STATE_EMPTY
					} else {
						binding.msvOfficers.viewState = VIEW_STATE_CONTENT
						if (binding.rvOfficers.adapter == null) {
							officersAdapter = OfficersAdapter(state.officerItems, OfficersTypeFactory())
							binding.rvOfficers.adapter = officersAdapter
						} else {
							officersAdapter?.updateItems(state.officerItems)
						}
					}
					observeActions()
				}
				else -> {
				}
			}
		}
	}
}
