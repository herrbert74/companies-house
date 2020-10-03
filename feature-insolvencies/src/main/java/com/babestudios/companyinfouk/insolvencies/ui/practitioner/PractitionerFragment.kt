package com.babestudios.companyinfouk.insolvencies.ui.practitioner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.mvrx.BaseFragment
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.insolvencies.databinding.FragmentPractitionerBinding
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesActivity
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesState
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesViewModel
import io.reactivex.disposables.CompositeDisposable

class PractitionerFragment : BaseFragment() {

	private val viewModel by existingViewModel(InsolvenciesViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentPractitionerBinding? = null
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
		_binding = FragmentPractitionerBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabPractitioner.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabPractitioner.setNavigationOnClickListener { viewModel.insolvenciesNavigator.popBackStack() }
		toolBar?.setTitle(R.string.practitioner_details)
		viewModel.selectSubscribe(InsolvenciesState::insolvencyDetailsItems) {
			showPractitioner()
		}
		viewModel.convertCaseToDetailsVisitables()
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
		val activity = requireActivity() as InsolvenciesActivity
		viewModel.setNavigator(activity.injectInsolvenciesNavigator())
	}

	//endregion

	//region render

	private fun showPractitioner() {
		withState(viewModel) {state->
			state.selectedPractitioner?.run {
				if (appointedOn == null) {
					binding.twoLinePractitionerAppointedOn.visibility = View.GONE
				} else {
					binding.twoLinePractitionerAppointedOn.visibility = View.VISIBLE
					binding.twoLinePractitionerAppointedOn.setTextSecond(appointedOn)
				}
				if (ceasedToActOn == null) {
					binding.twoLinePractitionerCeasedToActOn.visibility = View.GONE
				} else {
					binding.twoLinePractitionerCeasedToActOn.visibility = View.VISIBLE
					binding.twoLinePractitionerCeasedToActOn.setTextSecond(ceasedToActOn)
				}
				binding.twoLinePractitionerName.setTextSecond(name)
				binding.lblPractitionerAddressLine1.text = address.addressLine1
				binding.lblPractitionerLocality.text = address.locality
				binding.lblPractitionerPostalCode.text = address.postalCode
				if (address.region == null) {
					binding.lblPractitionerRegion.visibility = View.GONE
				} else {
					binding.lblPractitionerRegion.visibility = View.VISIBLE
					binding.lblPractitionerRegion.text = address.region
				}
				if (address.country == null) {
					binding.lblPractitionerCountry.visibility = View.GONE
				} else {
					binding.lblPractitionerCountry.visibility = View.VISIBLE
					binding.lblPractitionerCountry.text = address.country
				}
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
	}

	//endregion
}
