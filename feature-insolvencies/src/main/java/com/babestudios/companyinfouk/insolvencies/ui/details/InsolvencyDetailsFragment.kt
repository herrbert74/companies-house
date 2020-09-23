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
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.base.view.MultiStateView.VIEW_STATE_CONTENT
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.insolvencies.databinding.FragmentInsolvencyDetailsBinding
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesState
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesViewModel
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsAdapter
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsPractitionerViewHolder
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsTypeFactory
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsVisitableBase
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvencyViewHolder
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvencyVisitableBase
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class InsolvencyDetailsFragment : BaseMvRxFragment() {

	private var insolvencyDetailsAdapter: InsolvencyDetailsAdapter? = null

	private val viewModel by existingViewModel(InsolvenciesViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentInsolvencyDetailsBinding? = null
	private val binding get() = _binding!!

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
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		_binding = FragmentInsolvencyDetailsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabInsolvencyDetails.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabInsolvencyDetails.setNavigationOnClickListener { viewModel.insolvenciesNavigator.popBackStack() }
		toolBar?.setTitle(R.string.insolvency_details)
		createRecyclerView()
		viewModel.selectSubscribe(InsolvenciesState::insolvencyDetailsItems) {
			showInsolvencyCaseDetails()
		}
		viewModel.convertCaseToDetailsVisitables()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvInsolvencyDetails.layoutManager = linearLayoutManager
	}

	override fun onDestroyView() {
		super.onDestroyView()
		callback.remove()
		_binding = null
	}

	//endregion

	//region render

	private fun showInsolvencyCaseDetails() {
		binding.msvInsolvencyDetails.viewState = VIEW_STATE_CONTENT
		withState(viewModel) {state->
			if (binding.rvInsolvencyDetails.adapter == null) {
				val titlePositions = ArrayList<Int>()
				titlePositions.add(0)
				titlePositions.add(state.insolvencyCase.dates.size + 1)
				binding.rvInsolvencyDetails.addItemDecoration(
						DividerItemDecorationWithSubHeading(requireContext(), titlePositions))
				insolvencyDetailsAdapter = InsolvencyDetailsAdapter(state.insolvencyDetailsItems, InsolvencyDetailsTypeFactory())
				binding.rvInsolvencyDetails.adapter = insolvencyDetailsAdapter
				observeActions()
			} else {
				insolvencyDetailsAdapter?.updateItems(state.insolvencyDetailsItems)
				observeActions()
			}
		}
	}

	@Suppress("EmptyFunctionBlock")
	override fun invalidate() {

	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		insolvencyDetailsAdapter?.getViewClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<InsolvencyDetailsVisitableBase> ->
					viewModel.practitionerClicked((view as InsolvencyDetailsPractitionerViewHolder).adapterPosition)
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion
}
