package com.babestudios.companyinfouk.insolvencies.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.base.view.MultiStateView.VIEW_STATE_CONTENT
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesViewModel
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsAdapter
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsTypeFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_insolvency_details.*
import java.util.*

class InsolvencyDetailsFragment : BaseMvRxFragment() {

	private var insolvencyDetailsAdapter: InsolvencyDetailsAdapter? = null

	private val viewModel by existingViewModel(InsolvenciesViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private val callback: OnBackPressedCallback = (object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			viewModel.insolvenciesNavigator.popBackStack()
		}
	})

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		requireActivity().onBackPressedDispatcher.addCallback(this, callback)
		return inflater.inflate(R.layout.fragment_insolvency_details, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(pabInsolvencyDetails.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		pabInsolvencyDetails.setNavigationOnClickListener { viewModel.insolvenciesNavigator.popBackStack() }
		toolBar?.setTitle(R.string.insolvency_details)
		createRecyclerView()
		showInsolvencyCase()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		rvInsolvencyDetails?.layoutManager = linearLayoutManager
	}

	override fun onDestroyView() {
		super.onDestroyView()
		callback.remove()
	}

	//endregion

	//region render

	private fun showInsolvencyCase() {
		msvInsolvencyDetails.viewState = VIEW_STATE_CONTENT
		withState(viewModel) {state->
			if (rvInsolvencyDetails?.adapter == null) {
				val titlePositions = ArrayList<Int>()
				titlePositions.add(0)
				titlePositions.add(state.insolvencyCase.dates.size.let { it } ?: 0 + 1)
				rvInsolvencyDetails.addItemDecoration(
						DividerItemDecorationWithSubHeading(requireContext(), titlePositions))
				insolvencyDetailsAdapter = InsolvencyDetailsAdapter(state.insolvencyDetailsItems, InsolvencyDetailsTypeFactory())
				rvInsolvencyDetails?.adapter = insolvencyDetailsAdapter
				observeActions()
			} else {
				insolvencyDetailsAdapter?.updateItems(state.insolvencyDetailsItems)
				observeActions()
			}
		}
	}

	override fun invalidate() {

	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
	}

	//endregion
}
