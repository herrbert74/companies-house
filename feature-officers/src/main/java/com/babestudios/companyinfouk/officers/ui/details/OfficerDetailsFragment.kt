package com.babestudios.companyinfouk.officers.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.companyinfouk.officers.R
import com.babestudios.companyinfouk.officers.databinding.FragmentOfficerDetailsBinding
import com.babestudios.companyinfouk.officers.ui.OfficersViewModel
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable

class OfficerDetailsFragment : BaseMvRxFragment() {

	private val viewModel by existingViewModel(OfficersViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentOfficerDetailsBinding? = null
	private val binding get() = _binding!!

	private val callback: OnBackPressedCallback = (object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			viewModel.officersNavigator.popBackStack()
		}
	})

//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		requireActivity().onBackPressedDispatcher.addCallback(this, callback)
		_binding = FragmentOfficerDetailsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		val activity = (activity as AppCompatActivity)
		val toolbar = binding.pabOfficerDetails.getToolbar()
		activity.setSupportActionBar(toolbar)
		activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabOfficerDetails.setNavigationOnClickListener { viewModel.officersNavigator.popBackStack() }
		activity.supportActionBar?.setTitle(R.string.officer_details)
		showOfficerDetails()
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

//region events

	@Suppress("CheckResult")
	private fun observeActions() {
		eventDisposables.clear()
		val extras = FragmentNavigatorExtras(
				binding.btnOfficerDetailsAppointments to "pabOfficerAppointments")
		RxView.clicks(binding.btnOfficerDetailsAppointments)
				.subscribe { viewModel.officerAppointmentsClicked(extras) }
	}

	//endregion

	//region render

	@Suppress("EmptyFunctionBlock")
	override fun invalidate() {
	}

	private fun showOfficerDetails() {
		withState(viewModel) { state ->
			val officerItem = state.officerItem
			binding.lblOfficerDetailsName.text = officerItem?.name
			binding.lblOfficerDetailsAppointedOn.text = officerItem?.appointedOn
			binding.lblOfficerDetailsNationality.text = officerItem?.nationality
			binding.lblOfficerDetailsOccupation.text = officerItem?.occupation
			officerItem?.dateOfBirth?.let {
				binding.lblOfficerDetailsDateOfBirth.text = "${it.month.toString()} / ${it.year.toString()}"
			} ?: run {
				binding.lblOfficerDetailsDateOfBirth.setText(R.string.officer_details_unknown)
			}
			binding.lblOfficerDetailsCountryOfResidence.text = officerItem?.countryOfResidence
			binding.lblOfficerDetailsAddressLine1.text = officerItem?.address?.addressLine1
			binding.lblOfficerDetailsLocality.text = officerItem?.address?.locality
			binding.lblOfficerDetailsPostalCode.text = officerItem?.address?.postalCode
			officerItem?.address?.region?.let {
				binding.lblOfficerDetailsRegion.visibility = View.VISIBLE
				binding.lblOfficerDetailsRegion.text = it
			} ?: run {
				binding.lblOfficerDetailsRegion.visibility = View.GONE
			}
			officerItem?.address?.country?.let {
				binding.lblOfficerDetailsCountry.text = it
				binding.lblOfficerDetailsCountry.visibility = View.VISIBLE
			} ?: run {
				binding.lblOfficerDetailsCountry.visibility = View.GONE
			}
		}
	}

//endregion

}
