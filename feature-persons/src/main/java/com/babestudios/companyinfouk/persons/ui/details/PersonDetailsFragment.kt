package com.babestudios.companyinfouk.persons.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.mvrx.BaseFragment
import com.babestudios.companyinfouk.persons.R
import com.babestudios.companyinfouk.persons.databinding.FragmentPersonDetailsBinding
import com.babestudios.companyinfouk.persons.ui.PersonsActivity
import com.babestudios.companyinfouk.persons.ui.PersonsViewModel
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable

class PersonDetailsFragment : BaseFragment() {

	private val viewModel by existingViewModel(PersonsViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentPersonDetailsBinding? = null
	private val binding get() = _binding!!

	private val callback: OnBackPressedCallback = (object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			viewModel.personsNavigator.popBackStack()
		}
	})

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		_binding = FragmentPersonDetailsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabPersonDetails.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabPersonDetails.setNavigationOnClickListener { viewModel.personsNavigator.popBackStack() }
		toolBar?.setTitle(R.string.person_details)
		showPersonDetails()
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
		val activity = requireActivity() as PersonsActivity
		viewModel.setNavigator(activity.injectPersonsNavigator())
	}

	//endregion

	//region render

	@Suppress("ComplexMethod")
	private fun showPersonDetails() {
		withState(viewModel) { state ->
			state.selectedPerson?.let { person ->
				binding.twoLinePersonDetailsName.setTextSecond(person.name)
				binding.twoLinePersonDetailsNotifiedOn.isVisible = !person.notifiedOn.isBlank()
				binding.twoLinePersonDetailsNotifiedOn.setTextSecond(person.notifiedOn)
				binding.twoLinePersonDetailsKind.setTextSecond(person.kind)
				binding.twoLinePersonDetailsNaturesOfControl.setTextSecond(
						person.naturesOfControl.joinToString(separator = "\n"))
				binding.twoLinePersonDetailsNationality.isVisible = (person.nationality?.isBlank() == false)
				binding.twoLinePersonDetailsNationality.setTextSecond(person.nationality)
				binding.twoLinePersonDetailsDateOfBirth.isVisible = (person.dateOfBirth?.year != null)
				binding.twoLinePersonDetailsDateOfBirth.setTextSecond(
						"${person.dateOfBirth?.month} / ${person.dateOfBirth?.year}")
				binding.twoLinePersonDetailsCountryOfResidence.isVisible =
						(person.countryOfResidence?.isBlank() == false
								&& person.countryOfResidence != person.address.country)
				binding.twoLinePersonDetailsCountryOfResidence.setTextSecond(person.countryOfResidence)
				binding.twoLinePersonDetailsPlaceRegistered.isVisible =
						(person.identification?.placeRegistered?.isBlank() == false)
				binding.twoLinePersonDetailsPlaceRegistered.setTextSecond(person.identification?.placeRegistered)
				binding.twoLinePersonDetailsRegistrationNumber.isVisible =
						(person.identification?.registrationNumber?.isBlank() == false)
				binding.twoLinePersonDetailsRegistrationNumber.setTextSecond(person.identification?.registrationNumber)
				binding.twoLinePersonDetailsLegalForm.isVisible =
						(person.identification?.legalForm?.isBlank() == false)
				binding.twoLinePersonDetailsLegalForm.setTextSecond(
						person.identification?.legalForm)
				binding.twoLinePersonDetailsLegalAuthority.isVisible =
						(person.identification?.legalAuthority?.isBlank() == false)
				binding.twoLinePersonDetailsLegalAuthority.setTextSecond(
						person.identification?.legalAuthority)
				binding.addressViewPersonDetails.setAddressLine1(person.address.addressLine1)
				binding.addressViewPersonDetails.setAddressLine2(person.address.addressLine2)
				binding.addressViewPersonDetails.setLocality(person.address.locality)
				binding.addressViewPersonDetails.setPostalCode(person.address.postalCode)
				binding.addressViewPersonDetails.setRegion(person.address.region)
				binding.addressViewPersonDetails.setCountry(person.address.country)
			}
		}
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		RxView.clicks(binding.addressViewPersonDetails.getMapButton())
				.subscribe { viewModel.showOnMapClicked() }
				.also { disposable -> eventDisposables.add(disposable) }
	}

	@Suppress("EmptyFunctionBlock")
	override fun invalidate() {

	}

	//endregion
}
