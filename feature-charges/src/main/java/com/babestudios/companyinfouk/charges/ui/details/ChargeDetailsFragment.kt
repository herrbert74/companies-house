package com.babestudios.companyinfouk.charges.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.charges.ui.ChargesViewModel
import com.babestudios.companyinfouk.charges.ui.details.list.*
import com.babestudios.companyinfouk.data.model.charges.Transaction
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_charge_details.*

class ChargeDetailsFragment : BaseMvRxFragment() {

	private val viewModel by existingViewModel(ChargesViewModel::class)

	private var chargeDetailsAdapter: ChargeDetailsAdapter? = null

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {

		return inflater.inflate(R.layout.fragment_charge_details, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(pabChargeDetails.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		pabChargeDetails.setNavigationOnClickListener { viewModel.chargesNavigator.popBackStack() }
		toolBar?.setTitle(R.string.charge_details_title)
		createRecyclerView()
		showChargesItem()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	private fun showChargesItem() {
		withState(viewModel) { state ->
			state.chargesItem?.let { chargesItem ->
				tvChargeDetailsDeliveredOn?.text = chargesItem.deliveredOn
				tvChargeDetailsStatus?.text = chargesItem.status
				tvChargeDetailsContainsFloatingCharge?.text =
						if (chargesItem.particulars?.containsFloatingCharge == true) "YES" else "NO"
				tvChargeDetailsFloatingChargeCoversAll?.text =
						if (chargesItem.particulars?.floatingChargeCoversAll == true) "YES" else "NO"
				tvChargeDetailsContainsNegativePledge?.text =
						if (chargesItem.particulars?.containsNegativePledge == true) "YES" else "NO"
				tvChargeDetailsContainsFixedCharge?.text =
						if (chargesItem.particulars?.containsFixedCharge == true) "YES" else "NO"
				if (chargesItem.satisfiedOn == null) {
					lblChargeDetailsSatisfiedOn?.visibility = GONE
					tvChargeDetailsSatisfiedOn?.visibility = GONE
				} else {
					tvChargeDetailsSatisfiedOn?.visibility = VISIBLE
					lblChargeDetailsSatisfiedOn?.visibility = VISIBLE
					tvChargeDetailsSatisfiedOn?.text = chargesItem.satisfiedOn
				}
				tvChargeDetailsPersonsEntitled?.text = chargesItem.personsEntitled[0].name
			}
		}
	}

	private fun createRecyclerView() {
		withState(viewModel) { state ->
			state.chargesItem?.let { chargesItem ->
				val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
				rvChargeDetails.layoutManager = linearLayoutManager
				val titlePositions = java.util.ArrayList<Int>()
				titlePositions.add(0)
				rvChargeDetails.addItemDecoration(DividerItemDecorationWithSubHeading(requireContext(), titlePositions))
				chargeDetailsAdapter = ChargeDetailsAdapter(convertToVisitables(chargesItem.transactions), ChargeDetailsTypeFactory())
				rvChargeDetails.adapter = chargeDetailsAdapter
			}
		}
	}

	private fun convertToVisitables(transactions: List<Transaction>): List<AbstractChargeDetailsVisitable> {
		val visitables = ArrayList<AbstractChargeDetailsVisitable>()
		visitables.add(ChargeDetailsHeaderVisitable(ChargeDetailsHeaderItem(getString(R.string.transactions))))
		visitables.addAll(transactions.map { item -> ChargeDetailsVisitable(item) })
		return visitables
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
	}

	override fun invalidate() {

	}

	//endregion
}
