package com.babestudios.companyinfouk.persons.ui.details

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.babestudios.companyinfouk.common.ext.viewBinding
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.persons.R
import com.babestudios.companyinfouk.persons.databinding.FragmentPersonDetailsBinding
import com.babestudios.companyinfouk.persons.ui.PersonsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.android.view.clicks

@AndroidEntryPoint
class PersonDetailsFragment : Fragment(R.layout.fragment_person_details) {

	@Inject
	lateinit var companiesRepository: CompaniesRepository

	private val args: PersonDetailsFragmentArgs by navArgs()

	private val selectedPerson: Person by lazy {
		args.selectedPerson
	}

	private val binding by viewBinding<FragmentPersonDetailsBinding>()

	private val callback: OnBackPressedCallback = (object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			(activity as PersonsActivity).personsNavigator.popBackStack()
		}
	})

	//region life cycle

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		initializeUI()
	}

	private fun initializeUI() {
		companiesRepository.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabPersonDetails.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabPersonDetails.setNavigationOnClickListener {
			(activity as PersonsActivity).personsNavigator.popBackStack()
		}
		toolBar?.setTitle(R.string.person_details)
		showPersonDetails()
		lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
				binding.addressViewPersonDetails.getMapButton().clicks().onEach {
					(activity as PersonsActivity).personsNavigator.personDetailsToMap(
						selectedPerson.name	, selectedPerson.address
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

	//region render

	@Suppress("ComplexMethod")
	private fun showPersonDetails() {
		binding.twoLinePersonDetailsName.setTextSecond(selectedPerson.name)
		binding.twoLinePersonDetailsNotifiedOn.isVisible = selectedPerson.notifiedOn.isNotBlank()
		binding.twoLinePersonDetailsNotifiedOn.setTextSecond(selectedPerson.notifiedOn)
		binding.twoLinePersonDetailsKind.setTextSecond(selectedPerson.kind)
		binding.twoLinePersonDetailsNaturesOfControl.setTextSecond(
			selectedPerson.naturesOfControl.joinToString(separator = "\n")
		)
		binding.twoLinePersonDetailsNationality.isVisible = (selectedPerson.nationality?.isBlank() == false)
		binding.twoLinePersonDetailsNationality.setTextSecond(selectedPerson.nationality)
		binding.twoLinePersonDetailsDateOfBirth.isVisible = (selectedPerson.kind == "Individual")
		val (month, year) = selectedPerson.dateOfBirth.month to selectedPerson.dateOfBirth.year
		if (month == null || year == null) {
			binding.twoLinePersonDetailsDateOfBirth.setTextSecond(getString(R.string.officer_details_unknown))
		} else {
			binding.twoLinePersonDetailsDateOfBirth.setTextSecond("$month / $year")
		}
		binding.twoLinePersonDetailsCountryOfResidence.isVisible =
			(selectedPerson.countryOfResidence?.isBlank() == false
				&& selectedPerson.countryOfResidence != selectedPerson.address.country)
		binding.twoLinePersonDetailsCountryOfResidence.setTextSecond(selectedPerson.countryOfResidence)
		binding.twoLinePersonDetailsPlaceRegistered.isVisible =
			(selectedPerson.identification?.placeRegistered?.isBlank() == false)
		binding.twoLinePersonDetailsPlaceRegistered.setTextSecond(selectedPerson.identification?.placeRegistered)
		binding.twoLinePersonDetailsRegistrationNumber.isVisible =
			(selectedPerson.identification?.registrationNumber?.isBlank() == false)
		binding.twoLinePersonDetailsRegistrationNumber.setTextSecond(selectedPerson.identification?.registrationNumber)
		binding.twoLinePersonDetailsLegalForm.isVisible =
			(selectedPerson.identification?.legalForm?.isBlank() == false)
		binding.twoLinePersonDetailsLegalForm.setTextSecond(
			selectedPerson.identification?.legalForm
		)
		binding.twoLinePersonDetailsLegalAuthority.isVisible =
			(selectedPerson.identification?.legalAuthority?.isBlank() == false)
		binding.twoLinePersonDetailsLegalAuthority.setTextSecond(
			selectedPerson.identification?.legalAuthority
		)
		binding.addressViewPersonDetails.setAddressLine1(selectedPerson.address.addressLine1)
		binding.addressViewPersonDetails.setAddressLine2(selectedPerson.address.addressLine2)
		binding.addressViewPersonDetails.setLocality(selectedPerson.address.locality)
		binding.addressViewPersonDetails.setPostalCode(selectedPerson.address.postalCode)
		binding.addressViewPersonDetails.setRegion(selectedPerson.address.region)
		binding.addressViewPersonDetails.setCountry(selectedPerson.address.country)
	}

	//endregion

}
