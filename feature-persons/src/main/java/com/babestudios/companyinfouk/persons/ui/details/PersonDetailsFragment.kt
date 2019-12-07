package com.babestudios.companyinfouk.persons.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.companyinfouk.persons.R
import com.babestudios.companyinfouk.persons.ui.PersonsViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_person_details.*

class PersonDetailsFragment : BaseMvRxFragment() {

	private val viewModel by existingViewModel(PersonsViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

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
		requireActivity().onBackPressedDispatcher.addCallback(this, callback)
		return inflater.inflate(R.layout.fragment_person_details, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(pabPersonDetails.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		pabPersonDetails.setNavigationOnClickListener { viewModel.personsNavigator.popBackStack() }
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
	}

	//endregion

	//region render

	@Suppress("ComplexMethod")
	private fun showPersonDetails() {
		withState(viewModel) { state ->
			state.personItem?.let { person ->
				lblPersonDetailsName?.text = person.name
				lblPersonDetailsNotifiedOn?.text = person.notifiedOn
				if (person.nationality == null) {
					lblPersonDetailsNationality?.visibility = View.GONE
					cpnPersonDetailsNationality?.visibility = View.GONE
				} else {
					lblPersonDetailsNationality?.visibility = View.VISIBLE
					lblPersonDetailsNationality?.text = person.nationality
				}
				if (person.countryOfResidence == null) {
					lblPersonDetailsCountryOfResidence?.visibility = View.GONE
					cpnPersonDetailsCountryOfResidence?.visibility = View.GONE
				} else {
					lblPersonDetailsCountryOfResidence?.visibility = View.VISIBLE
					lblPersonDetailsCountryOfResidence?.text = person.countryOfResidence
				}
				person.dateOfBirth?.let {
					lblPersonDetailsDateOfBirth?.visibility = View.VISIBLE
					lblPersonDetailsDateOfBirth?.text = "${it.month.toString()} / ${it.year.toString()}"
				} ?: run {
					lblPersonDetailsDateOfBirth?.visibility = View.GONE
					cpnPersonDetailsDateOfBirth?.visibility = View.GONE
				}
				val naturesOfControl = StringBuilder()
				for (i in person.naturesOfControl.indices) {
					naturesOfControl.append(person.naturesOfControl[i])
					if (i < person.naturesOfControl.size - 1) {
						naturesOfControl.append("; ")
					}
				}
				lblPersonDetailsNaturesOfControl?.text = naturesOfControl.toString()

				lblPersonDetailsAddress?.text = person.address?.addressLine1
				lblPersonDetailsLocality?.text = person.address?.locality
				lblPersonDetailsPostalCode?.text = person.address?.postalCode
				person.identification?.placeRegistered?.let {
					lblPersonDetailsPlaceRegistered?.text = it
					lblPersonDetailsPlaceRegistered?.visibility = View.VISIBLE
				} ?: run {
					lblPersonDetailsPlaceRegistered?.visibility = View.GONE
					cpnPersonDetailsPlaceRegistered?.visibility = View.GONE
				}
				person.identification?.registrationNumber?.let {
					lblPersonDetailsRegistrationNumber?.text = it
					lblPersonDetailsRegistrationNumber?.visibility = View.VISIBLE
				} ?: run {
					cpnPersonDetailsRegistrationNumber?.visibility = View.GONE
					lblPersonDetailsRegistrationNumber?.visibility = View.GONE
				}
				if (person.address?.region == null) {
					lblPersonDetailsRegion?.visibility = View.GONE
				} else {
					lblPersonDetailsRegion?.visibility = View.VISIBLE
					lblPersonDetailsRegion?.text = person.address?.region
				}
				person.address?.country?.let {
					lblPersonDetailsCountry?.text = it
					lblPersonDetailsCountry?.visibility = View.VISIBLE
				} ?: run {
					lblPersonDetailsCountry?.visibility = View.GONE
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
