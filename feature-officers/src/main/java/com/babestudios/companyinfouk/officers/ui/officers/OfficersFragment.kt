package com.babestudios.companyinfouk.officers.ui.officers

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
import com.babestudios.companyinfouk.officers.R
import com.babestudios.companyinfouk.officers.ui.OfficersViewModel
import com.babestudios.companyinfouk.officers.ui.officers.list.AbstractOfficersVisitable
import com.babestudios.companyinfouk.officers.ui.officers.list.OfficersAdapter
import com.babestudios.companyinfouk.officers.ui.officers.list.OfficersTypeFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_officers.*

class OfficersFragment : BaseMvRxFragment() {

	private var officersAdapter: OfficersAdapter? = null

	private val viewModel by activityViewModel(OfficersViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {

		return inflater.inflate(R.layout.fragment_officers, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(pabOfficers.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		pabOfficers.setNavigationOnClickListener { activity?.onBackPressed() }
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

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
		rvOfficers?.layoutManager = linearLayoutManager
		rvOfficers.addItemDecoration(DividerItemDecoration(requireContext()))
		rvOfficers.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
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
				?.subscribe { view: BaseViewHolder<AbstractOfficersVisitable> ->
					viewModel.officerItemClicked(view.adapterPosition)
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.officersRequest) {
				is Loading -> msvOfficers.viewState = VIEW_STATE_LOADING
				is Fail -> {
					msvOfficers.viewState = VIEW_STATE_ERROR
					val tvMsvError = msvOfficers.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.officersRequest.error.message
				}
				is Success -> {
					if (state.officerItems.isEmpty()) {
						msvOfficers.viewState = VIEW_STATE_EMPTY
					} else {
						msvOfficers.viewState = VIEW_STATE_CONTENT
						if (rvOfficers?.adapter == null) {
							officersAdapter = OfficersAdapter(state.officerItems, OfficersTypeFactory())
							rvOfficers?.adapter = officersAdapter
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
