package com.babestudios.companyinfouk.charges.ui.charges

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
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.charges.ui.ChargesViewModel
import com.babestudios.companyinfouk.charges.ui.charges.list.AbstractChargesVisitable
import com.babestudios.companyinfouk.charges.ui.charges.list.ChargesAdapter
import com.babestudios.companyinfouk.charges.ui.charges.list.ChargesTypeFactory
import com.babestudios.companyinfouk.charges.ui.charges.list.ChargesViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_charges.*

class ChargesFragment : BaseMvRxFragment() {

	private val viewModel by activityViewModel(ChargesViewModel::class)

	private var chargesAdapter: ChargesAdapter? = null

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {

		return inflater.inflate(R.layout.fragment_charges, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(pabCharges.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		pabCharges.setNavigationOnClickListener { activity?.onBackPressed() }
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
		rvCharges?.layoutManager = linearLayoutManager
		rvCharges?.addItemDecoration(DividerItemDecoration(requireContext()))
		rvCharges?.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				viewModel.loadMoreCharges(page)
			}
		})
	}

	//endregion

	//region render

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		chargesAdapter?.getViewClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<AbstractChargesVisitable> ->
					viewModel.chargesItemClicked((view as ChargesViewHolder).adapterPosition)
				}
				?.let { eventDisposables.add(it) }
	}

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.chargesRequest) {
				is Loading -> msvCharges.viewState = VIEW_STATE_LOADING
				is Fail -> {
					msvCharges.viewState = VIEW_STATE_ERROR
					val tvMsvError = msvCharges.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.chargesRequest.error.message
				}
				is Success -> {
					if (state.charges.isEmpty()) {
						msvCharges.viewState = VIEW_STATE_EMPTY
					} else {
						msvCharges.viewState = VIEW_STATE_CONTENT
						if (rvCharges?.adapter == null) {
							chargesAdapter = ChargesAdapter(state.charges, ChargesTypeFactory())
							rvCharges?.adapter = chargesAdapter
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
