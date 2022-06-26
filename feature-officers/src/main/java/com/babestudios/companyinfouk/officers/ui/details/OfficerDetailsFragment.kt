package com.babestudios.companyinfouk.officers.ui.details

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.babestudios.companyinfouk.common.ext.viewBinding
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.common.getAddressString
import com.babestudios.companyinfouk.navigation.DeepLinkDestination
import com.babestudios.companyinfouk.navigation.deepLinkNavigateTo
import com.babestudios.companyinfouk.navigation.navigateSafe
import com.babestudios.companyinfouk.officers.R
import com.babestudios.companyinfouk.officers.databinding.FragmentOfficerDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.android.view.clicks

@AndroidEntryPoint
class OfficerDetailsFragment : Fragment(R.layout.fragment_officer_details) {

	@Inject
	lateinit var companiesRepository: CompaniesRepository

	private val args: OfficerDetailsFragmentArgs by navArgs()

	private val binding by viewBinding<FragmentOfficerDetailsBinding>()

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
		initializeClicks()
		showOfficerDetails()
	}

	private fun initializeToolBar() {
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		(activity as AppCompatActivity).setSupportActionBar(binding.pabOfficerDetails.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabOfficerDetails.setNavigationOnClickListener {
			findNavController().popBackStack()
		}
		toolBar?.setTitle(R.string.officer_details)
	}

	private fun initializeClicks() {
		lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
				binding.btnOfficerDetailsAppointments.clicks().onEach {
					findNavController().navigateSafe(
						OfficerDetailsFragmentDirections.actionToAppointments(
							args.selectedCompanyId, args.selectedOfficer
						)
					)
				}.launchIn(lifecycleScope)
				binding.addressViewOfficerDetails.getMapButton().clicks().onEach {
					findNavController().deepLinkNavigateTo(
						DeepLinkDestination.Map(
							args.selectedOfficer.name, args.selectedOfficer.address.getAddressString(),
						)
					)
				}.launchIn(lifecycleScope)
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		callback.remove()
	}

	//endregion

	private fun showOfficerDetails() {
		val officer = args.selectedOfficer
		binding.twoLineOfficerDetailsName.setTextSecond(officer.name)
		binding.twoLineOfficerDetailsAppointedOn.setTextSecond(officer.appointedOn)
		binding.twoLineOfficerDetailsNationality.setTextSecond(officer.nationality)
		binding.twoLineOfficerDetailsOccupation.setTextSecond(officer.occupation)

		val (month, year) = officer.dateOfBirth.month to officer.dateOfBirth?.year
		if (month == null || year == null) {
			binding.twoLineOfficerDetailsDateOfBirth.setTextSecond(getString(R.string.officer_details_unknown))
		} else {
			binding.twoLineOfficerDetailsDateOfBirth.setTextSecond("$month / $year")
		}

		binding.twoLineOfficerDetailsCountryOfResidence.setTextSecond(officer.countryOfResidence)
		binding.addressViewOfficerDetails.setAddressLine1(officer.address.addressLine1)
		binding.addressViewOfficerDetails.setAddressLine2(officer.address.addressLine2)
		binding.addressViewOfficerDetails.setLocality(officer.address.locality)
		binding.addressViewOfficerDetails.setPostalCode(officer.address.postalCode)
		binding.addressViewOfficerDetails.setRegion(officer.address.region)
		binding.addressViewOfficerDetails.setCountry(officer.address.country)
	}

}
