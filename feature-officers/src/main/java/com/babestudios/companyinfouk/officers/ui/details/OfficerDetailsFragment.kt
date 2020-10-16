package com.babestudios.companyinfouk.officers.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.ext.biLet
import com.babestudios.base.mvrx.BaseFragment
import com.babestudios.companyinfouk.officers.R
import com.babestudios.companyinfouk.officers.databinding.FragmentOfficerDetailsBinding
import com.babestudios.companyinfouk.officers.ui.OfficersActivity
import com.babestudios.companyinfouk.officers.ui.OfficersViewModel
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable

class OfficerDetailsFragment : BaseFragment() {

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
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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

	override fun orientationChanged() {
		val activity = requireActivity() as OfficersActivity
		viewModel.setNavigator(activity.injectOfficersNavigator())
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
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(binding.addressViewOfficerDetails.getMapButton())
				.subscribe { viewModel.showOnMapClicked() }
				.also { disposable -> eventDisposables.add(disposable) }
	}

	//endregion

	//region render

	@Suppress("EmptyFunctionBlock")
	override fun invalidate() {
	}

	private fun showOfficerDetails() {
		withState(viewModel) { state ->
			val officerItem = state.selectedOfficer
			binding.twoLineOfficerDetailsName.setTextSecond(officerItem?.name)
			binding.twoLineOfficerDetailsAppointedOn.setTextSecond(officerItem?.appointedOn)
			binding.twoLineOfficerDetailsNationality.setTextSecond(officerItem?.nationality)
			binding.twoLineOfficerDetailsOccupation.setTextSecond(officerItem?.occupation)
			(officerItem?.dateOfBirth?.month to officerItem?.dateOfBirth?.year).biLet { month, year ->
				binding.twoLineOfficerDetailsDateOfBirth.setTextSecond("$month / $year")
			} ?: run {
				binding.twoLineOfficerDetailsDateOfBirth.setTextSecond(getString(R.string.officer_details_unknown))
			}
			binding.twoLineOfficerDetailsCountryOfResidence.setTextSecond(officerItem?.countryOfResidence)
			binding.addressViewOfficerDetails.setAddressLine1(officerItem?.address?.addressLine1)
			binding.addressViewOfficerDetails.setAddressLine2(officerItem?.address?.addressLine2)
			binding.addressViewOfficerDetails.setLocality(officerItem?.address?.locality)
			binding.addressViewOfficerDetails.setPostalCode(officerItem?.address?.postalCode)
			binding.addressViewOfficerDetails.setRegion(officerItem?.address?.region)
			binding.addressViewOfficerDetails.setCountry(officerItem?.address?.country)
		}
	}

//endregion

}
