package com.babestudios.companyinfouk.charges.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.charges.databinding.FragmentChargeDetailsBinding
import com.babestudios.companyinfouk.charges.ui.ChargesViewModel
import com.babestudios.companyinfouk.charges.ui.details.list.*
import com.babestudios.companyinfouk.data.model.charges.Transaction
import io.reactivex.disposables.CompositeDisposable

class ChargeDetailsFragment : BaseMvRxFragment() {

	private val viewModel by existingViewModel(ChargesViewModel::class)

	private var chargeDetailsAdapter: ChargeDetailsAdapter? = null

	private var _binding: FragmentChargeDetailsBinding? = null
	private val binding get() = _binding!!

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private val callback: OnBackPressedCallback = (object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			viewModel.chargesNavigator.popBackStack()
		}
	})

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		requireActivity().onBackPressedDispatcher.addCallback(this, callback)
		_binding = FragmentChargeDetailsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabChargeDetails.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabChargeDetails.setNavigationOnClickListener { viewModel.chargesNavigator.popBackStack() }
		toolBar?.setTitle(R.string.charge_details_title)
		createRecyclerView()
		showChargesItem()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		callback.remove()
		_binding = null
	}


	//endregion

	//region render

	private fun showChargesItem() {
		withState(viewModel) { state ->
			state.chargesItem?.let { chargesItem ->
				binding.tvChargeDetailsDeliveredOn.text = chargesItem.deliveredOn
				binding.tvChargeDetailsStatus.text = chargesItem.status
				binding.tvChargeDetailsContainsFloatingCharge.text =
						if (chargesItem.particulars?.containsFloatingCharge == true) "YES" else "NO"
				binding.tvChargeDetailsFloatingChargeCoversAll.text =
						if (chargesItem.particulars?.floatingChargeCoversAll == true) "YES" else "NO"
				binding.tvChargeDetailsContainsNegativePledge.text =
						if (chargesItem.particulars?.containsNegativePledge == true) "YES" else "NO"
				binding.tvChargeDetailsContainsFixedCharge.text =
						if (chargesItem.particulars?.containsFixedCharge == true) "YES" else "NO"
				if (chargesItem.satisfiedOn == null) {
					binding.lblChargeDetailsSatisfiedOn.visibility = GONE
					binding.tvChargeDetailsSatisfiedOn.visibility = GONE
				} else {
					binding.tvChargeDetailsSatisfiedOn.visibility = VISIBLE
					binding.lblChargeDetailsSatisfiedOn.visibility = VISIBLE
					binding.tvChargeDetailsSatisfiedOn.text = chargesItem.satisfiedOn
				}
				binding.tvChargeDetailsPersonsEntitled.text = chargesItem.personsEntitled[0].name
			}
		}
	}

	private fun createRecyclerView() {
		withState(viewModel) { state ->
			state.chargesItem?.let { chargesItem ->
				val linearLayoutManager = LinearLayoutManager(
						requireContext(),
						LinearLayoutManager.VERTICAL,
						false
				)
				binding.rvChargeDetails.layoutManager = linearLayoutManager
				val titlePositions = java.util.ArrayList<Int>()
				titlePositions.add(0)
				binding.rvChargeDetails
						.addItemDecoration(DividerItemDecorationWithSubHeading(requireContext(), titlePositions))
				chargeDetailsAdapter = ChargeDetailsAdapter(
						convertToVisitables(chargesItem.transactions),
						ChargeDetailsTypeFactory()
				)
				binding.rvChargeDetails.adapter = chargeDetailsAdapter
			}
		}
	}

	private fun convertToVisitables(transactions: List<Transaction>): List<ChargeDetailsVisitableBase> {
		val visitables = ArrayList<ChargeDetailsVisitableBase>()
		visitables.add(ChargeDetailsVisitableBase.ChargeDetailsHeaderVisitable(
				ChargeDetailsHeaderItem(getString(R.string.transactions))))
		visitables.addAll(transactions.map { item -> ChargeDetailsVisitableBase.ChargeDetailsVisitable(item) })
		return visitables
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
	}

	@Suppress("EmptyFunctionBlock")
	override fun invalidate() {

	}

	//endregion
}
