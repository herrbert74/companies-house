package com.babestudios.companyinfouk.charges.ui.details

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.charges.databinding.FragmentChargeDetailsBinding
import com.babestudios.companyinfouk.charges.ui.details.list.ChargeDetailsAdapter
import com.babestudios.companyinfouk.charges.ui.details.list.ChargeDetailsHeaderItem
import com.babestudios.companyinfouk.charges.ui.details.list.ChargeDetailsHeaderVisitable
import com.babestudios.companyinfouk.charges.ui.details.list.ChargeDetailsTypeFactory
import com.babestudios.companyinfouk.charges.ui.details.list.ChargeDetailsVisitable
import com.babestudios.companyinfouk.charges.ui.details.list.ChargeDetailsVisitableBase
import com.babestudios.companyinfouk.common.ext.viewBinding
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.charges.ChargesItem
import com.babestudios.companyinfouk.domain.model.charges.Transaction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChargeDetailsFragment : Fragment(R.layout.fragment_charge_details) {

	@Inject
	lateinit var companiesRepository: CompaniesRepository

	private val args: ChargeDetailsFragmentArgs by navArgs()

	private var chargeDetailsAdapter: ChargeDetailsAdapter? = null

	private val binding by viewBinding<FragmentChargeDetailsBinding>()

	private val selectedChargesItem: ChargesItem by lazy {
		args.selectedChargesItem
	}

	private val callback: OnBackPressedCallback = (object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			findNavController().popBackStack()
		}
	})

	//region life cycle

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		companiesRepository.logScreenView(this::class.simpleName.orEmpty())
		initializeToolBar()
		createRecyclerView()
		showChargesItem()
	}

	private fun initializeToolBar() {
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		(activity as AppCompatActivity).setSupportActionBar(binding.pabChargeDetails.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabChargeDetails.setNavigationOnClickListener { findNavController().popBackStack() }
		toolBar?.setTitle(R.string.charge_details_title)

	}

	override fun onDestroyView() {
		super.onDestroyView()
		callback.remove()
	}

	//endregion

	//region render

	private fun showChargesItem() {
			binding.lblChargeDetailsDeliveredOn.text = selectedChargesItem.deliveredOn
			binding.lblChargeDetailsStatus.text = selectedChargesItem.status
			if (selectedChargesItem.particulars.containsFloatingCharge == null) {
				binding.groupChargeDetails.visibility = GONE
			}
			binding.lblChargeDetailsContainsFloatingCharge.text =
				if (selectedChargesItem.particulars.containsFloatingCharge == true) "YES" else "NO"
			binding.lblChargeDetailsFloatingChargeCoversAll.text =
				if (selectedChargesItem.particulars.floatingChargeCoversAll == true) "YES" else "NO"
			binding.lblChargeDetailsContainsNegativePledge.text =
				if (selectedChargesItem.particulars.containsNegativePledge == true) "YES" else "NO"
			binding.lblChargeDetailsContainsFixedCharge.text =
				if (selectedChargesItem.particulars.containsFixedCharge == true) "YES" else "NO"
			if (selectedChargesItem.satisfiedOn.isEmpty()) {
				binding.cpnChargeDetailsSatisfiedOn.visibility = GONE
				binding.lblChargeDetailsSatisfiedOn.visibility = GONE
			} else {
				binding.cpnChargeDetailsSatisfiedOn.visibility = VISIBLE
				binding.lblChargeDetailsSatisfiedOn.visibility = VISIBLE
				binding.lblChargeDetailsSatisfiedOn.text = selectedChargesItem.satisfiedOn
			}
			binding.lblChargeDetailsPersonsEntitled.text = selectedChargesItem.personsEntitled
	}

	private fun createRecyclerView() {
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
				convertToVisitables(selectedChargesItem.transactions),
				ChargeDetailsTypeFactory()
			)
			binding.rvChargeDetails.adapter = chargeDetailsAdapter
	}

	private fun convertToVisitables(transactions: List<Transaction>): List<ChargeDetailsVisitableBase> {
		val visitables = ArrayList<ChargeDetailsVisitableBase>()
		visitables.add(
			ChargeDetailsHeaderVisitable(
				ChargeDetailsHeaderItem(getString(R.string.transactions))
			)
		)
		visitables.addAll(transactions.map { item -> ChargeDetailsVisitable(item) })
		return visitables
	}

	//endregion
}
