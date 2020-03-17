package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

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
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.insolvencies.databinding.FragmentInsolvencyBinding
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesViewModel
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.AbstractInsolvencyVisitable
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvenciesAdapter
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvenciesTypeFactory
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvencyViewHolder
import io.reactivex.disposables.CompositeDisposable

class InsolvenciesFragment : BaseMvRxFragment() {

	private var insolvenciesAdapter: InsolvenciesAdapter? = null

	private val viewModel by activityViewModel(InsolvenciesViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentInsolvencyBinding? = null
	private val binding get() = _binding!!

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		_binding = FragmentInsolvencyBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {

		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabInsolvency.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabInsolvency.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.setTitle(R.string.insolvency)
		createRecyclerView()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvInsolvency.layoutManager = linearLayoutManager
		binding.rvInsolvency.addItemDecoration(DividerItemDecoration(requireContext()))
	}

	//endregion

	//region render

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.insolvencyRequest) {
				is Loading -> binding.msvInsolvency.viewState = VIEW_STATE_LOADING
				is Fail -> {
					binding.msvInsolvency.viewState = VIEW_STATE_ERROR
					val tvMsvError = binding.msvInsolvency.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.insolvencyRequest.error.message
				}
				is Success -> {
					if (state.insolvencies.isEmpty()) {
						binding.msvInsolvency.viewState = VIEW_STATE_EMPTY
					} else {
						binding.msvInsolvency.viewState = VIEW_STATE_CONTENT
						if (binding.rvInsolvency.adapter == null) {
							insolvenciesAdapter = InsolvenciesAdapter(state.insolvencies, InsolvenciesTypeFactory())
							binding.rvInsolvency.adapter = insolvenciesAdapter
						} else {
							insolvenciesAdapter?.updateItems(state.insolvencies)
						}
						observeActions()
					}
				}
			}
		}
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		insolvenciesAdapter?.getViewClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<AbstractInsolvencyVisitable> ->
					viewModel.insolvencyItemClicked((view as InsolvencyViewHolder).adapterPosition)
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion
}
