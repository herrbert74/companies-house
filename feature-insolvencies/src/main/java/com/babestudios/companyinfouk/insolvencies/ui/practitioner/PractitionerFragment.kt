package com.babestudios.companyinfouk.insolvencies.ui.practitioner

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.babestudios.base.ext.viewBinding
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.insolvencies.databinding.FragmentPractitionerBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PractitionerFragment : Fragment(R.layout.fragment_practitioner) {

	@Inject
	lateinit var companiesRepository: CompaniesRepository

	private val binding by viewBinding<FragmentPractitionerBinding>()

	private val args: PractitionerFragmentArgs by navArgs()

	private val callback: OnBackPressedCallback = (object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			findNavController().popBackStack()
		}
	})

	//region life cycle

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		companiesRepository.logScreenView(this::class.simpleName.orEmpty())
		initializeToolBar()
		showPractitioner()
	}

	private fun initializeToolBar() {
		(activity as AppCompatActivity).setSupportActionBar(binding.pabPractitioner.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabPractitioner.setNavigationOnClickListener { findNavController().popBackStack() }
		toolBar?.setTitle(R.string.practitioner_details)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		callback.remove()
	}

	//endregion

	//region render

	private fun showPractitioner() {
		with(args.selectedPractitioner) {
			if (appointedOn == null) {
				binding.twoLinePractitionerAppointedOn.visibility = View.GONE
			} else {
				binding.twoLinePractitionerAppointedOn.visibility = View.VISIBLE
				binding.twoLinePractitionerAppointedOn.setTextSecond(
					appointedOn ?: getText(R.string.officer_details_unknown).toString()
				)
			}
			if (ceasedToActOn == null) {
				binding.twoLinePractitionerCeasedToActOn.visibility = View.GONE
			} else {
				binding.twoLinePractitionerCeasedToActOn.visibility = View.VISIBLE
				binding.twoLinePractitionerCeasedToActOn.setTextSecond(
					ceasedToActOn ?: getText(R.string.officer_details_unknown).toString()
				)
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

	//endregion

}
