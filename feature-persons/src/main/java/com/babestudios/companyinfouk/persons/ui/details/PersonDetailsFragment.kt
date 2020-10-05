package com.babestudios.companyinfouk.persons.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.mvrx.BaseFragment
import com.babestudios.companyinfouk.persons.R
import com.babestudios.companyinfouk.persons.databinding.FragmentPersonDetailsBinding
import com.babestudios.companyinfouk.persons.ui.PersonsActivity
import com.babestudios.companyinfouk.persons.ui.PersonsViewModel
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
			state.personItem?.let { person ->
				binding.lblPersonDetailsName.text = person.name
				binding.lblPersonDetailsNotifiedOn.text = person.notifiedOn
				if (person.nationality == null) {
					binding.lblPersonDetailsNationality.visibility = View.GONE
					binding.cpnPersonDetailsNationality.visibility = View.GONE
				} else {
					binding.lblPersonDetailsNationality.visibility = View.VISIBLE
					binding.lblPersonDetailsNationality.text = person.nationality
				}
				if (person.countryOfResidence == null) {
					binding.lblPersonDetailsCountryOfResidence.visibility = View.GONE
					binding.cpnPersonDetailsCountryOfResidence.visibility = View.GONE
				} else {
					binding.lblPersonDetailsCountryOfResidence.visibility = View.VISIBLE
					binding.lblPersonDetailsCountryOfResidence.text = person.countryOfResidence
				}
				person.dateOfBirth?.let {
					binding.lblPersonDetailsDateOfBirth.visibility = View.VISIBLE
					binding.lblPersonDetailsDateOfBirth.text = "${it.month.toString()} / ${it.year.toString()}"
				} ?: run {
					binding.lblPersonDetailsDateOfBirth.visibility = View.GONE
					binding.cpnPersonDetailsDateOfBirth.visibility = View.GONE
				}
				val naturesOfControl = StringBuilder()
				for (i in person.naturesOfControl.indices) {
					naturesOfControl.append(person.naturesOfControl[i])
					if (i < person.naturesOfControl.size - 1) {
						naturesOfControl.append("; ")
					}
				}
				binding.lblPersonDetailsNaturesOfControl.text = naturesOfControl.toString()

				binding.lblPersonDetailsAddress.text = person.address.addressLine1
				binding.lblPersonDetailsLocality.text = person.address.locality
				binding.lblPersonDetailsPostalCode.text = person.address.postalCode
				person.identification?.placeRegistered?.let {
					binding.lblPersonDetailsPlaceRegistered.text = it
					binding.lblPersonDetailsPlaceRegistered.visibility = View.VISIBLE
				} ?: run {
					binding.lblPersonDetailsPlaceRegistered.visibility = View.GONE
					binding.cpnPersonDetailsPlaceRegistered.visibility = View.GONE
				}
				person.identification?.registrationNumber?.let {
					binding.lblPersonDetailsRegistrationNumber.text = it
					binding.lblPersonDetailsRegistrationNumber.visibility = View.VISIBLE
				} ?: run {
					binding.cpnPersonDetailsRegistrationNumber.visibility = View.GONE
					binding.lblPersonDetailsRegistrationNumber.visibility = View.GONE
				}
				if (person.address.region == null) {
					binding.lblPersonDetailsRegion.visibility = View.GONE
				} else {
					binding.lblPersonDetailsRegion.visibility = View.VISIBLE
					binding.lblPersonDetailsRegion.text = person.address.region
				}
				person.address.country?.let {
					binding.lblPersonDetailsCountry.text = it
					binding.lblPersonDetailsCountry.visibility = View.VISIBLE
				} ?: run {
					binding.lblPersonDetailsCountry.visibility = View.GONE
				}
			}
		}
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
