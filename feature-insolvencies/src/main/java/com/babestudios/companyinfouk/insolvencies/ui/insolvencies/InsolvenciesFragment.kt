package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesViewModel
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.AbstractInsolvencyVisitable
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvenciesAdapter
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvenciesTypeFactory
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvencyViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_insolvency.*

class InsolvenciesFragment : BaseMvRxFragment() {

	private var insolvenciesAdapter: InsolvenciesAdapter? = null

	private val viewModel by activityViewModel(InsolvenciesViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {

		return inflater.inflate(R.layout.fragment_insolvency, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {

		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(pabInsolvency.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		pabInsolvency.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.setTitle(R.string.insolvency)
		createRecyclerView()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		rvInsolvency?.layoutManager = linearLayoutManager
		rvInsolvency.addItemDecoration(DividerItemDecoration(requireContext()))
	}

	//endregion

	//region render

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.insolvencyRequest) {
				is Loading -> msvInsolvency.viewState = VIEW_STATE_LOADING
				is Fail -> {
					msvInsolvency.viewState = VIEW_STATE_ERROR
					val tvMsvError = msvInsolvency.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.insolvencyRequest.error.message
				}
				is Success -> {
					if (state.insolvencies.isEmpty()) {
						msvInsolvency.viewState = VIEW_STATE_EMPTY
					} else {
						msvInsolvency.viewState = VIEW_STATE_CONTENT
						if (rvInsolvency?.adapter == null) {
							insolvenciesAdapter = InsolvenciesAdapter(state.insolvencies, InsolvenciesTypeFactory())
							rvInsolvency?.adapter = insolvenciesAdapter
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
