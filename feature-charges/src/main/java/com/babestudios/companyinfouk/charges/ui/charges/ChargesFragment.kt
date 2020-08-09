package com.babestudios.companyinfouk.charges.ui.charges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.charges.databinding.FragmentChargesBinding
import com.babestudios.companyinfouk.charges.ui.ChargesViewModel
import com.babestudios.companyinfouk.charges.ui.charges.list.ChargesVisitableBase
import com.babestudios.companyinfouk.charges.ui.charges.list.ChargesAdapter
import com.babestudios.companyinfouk.charges.ui.charges.list.ChargesTypeFactory
import com.babestudios.companyinfouk.charges.ui.charges.list.ChargesViewHolder
import io.reactivex.disposables.CompositeDisposable

class ChargesFragment : BaseMvRxFragment() {

	private val viewModel by activityViewModel(ChargesViewModel::class)

	private var chargesAdapter: ChargesAdapter? = null

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentChargesBinding? = null
	private val binding get() = _binding!!

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		_binding = FragmentChargesBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabCharges.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabCharges.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.setTitle(R.string.charges)
		createRecyclerView()
		withState(viewModel) {
			viewModel.fetchCharges(it.companyNumber)
		}
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvCharges.layoutManager = linearLayoutManager
		binding.rvCharges.addItemDecoration(DividerItemDecoration(requireContext()))
		binding.rvCharges.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				viewModel.loadMoreCharges(page)
			}
		})
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	//endregion

	//region render

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		chargesAdapter?.getViewClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<ChargesVisitableBase> ->
					viewModel.chargesItemClicked((view as ChargesViewHolder).adapterPosition)
				}
				?.let { eventDisposables.add(it) }
	}

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.chargesRequest) {
				is Loading -> binding.msvCharges.viewState = VIEW_STATE_LOADING
				is Fail -> {
					binding.msvCharges.viewState = VIEW_STATE_ERROR
					val tvMsvError = binding.msvCharges.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.chargesRequest.error.message
				}
				is Success -> {
					if (state.charges.isEmpty()) {
						binding.msvCharges.viewState = VIEW_STATE_EMPTY
					} else {
						binding.msvCharges.viewState = VIEW_STATE_CONTENT
						if (binding.rvCharges.adapter == null) {
							chargesAdapter = ChargesAdapter(state.charges, ChargesTypeFactory())
							binding.rvCharges.adapter = chargesAdapter
						} else {
							chargesAdapter?.updateItems(state.charges)
						}
					}
					observeActions()
				}
				else -> {
				}
			}
		}
	}

	//endregion
}
