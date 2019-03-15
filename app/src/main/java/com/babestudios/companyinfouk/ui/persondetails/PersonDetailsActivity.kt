package com.babestudios.companyinfouk.ui.persondetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View

import kotlinx.android.synthetic.main.activity_person_details.*
import com.babestudios.companyinfouk.R
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.CompletableSource
import androidx.lifecycle.ViewModelProviders
import com.babestudios.companyinfouk.Injector
import com.babestudios.companyinfouk.data.model.persons.Person

import io.reactivex.disposables.CompositeDisposable

private const val PERSON_ITEM = "com.babestudios.companyinfouk.ui.person_item"

class PersonDetailsActivity : RxAppCompatActivity(), ScopeProvider {


	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(PersonDetailsViewModel::class.java) }

	private lateinit var personDetailsPresenter: PersonDetailsPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_person_details)
		setSupportActionBar(pabPersonDetails.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabPersonDetails.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.person_details)
		when {
			viewModel.state.value.person != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				savedInstanceState.getParcelable<PersonDetailsState>("STATE")?.let {
					with(viewModel.state.value) {

						person = it.person
					}
				}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value.person = intent.getParcelableExtra(PERSON_ITEM)
				initPresenter(viewModel)
			}
		}

		observeState()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putParcelable("STATE", viewModel.state.value)
		super.onSaveInstanceState(outState)
	}

	private fun initPresenter(viewModel: PersonDetailsViewModel) {
		if (!::personDetailsPresenter.isInitialized) {
			personDetailsPresenter = Injector.get().personDetailsPresenter()
			personDetailsPresenter.setViewModel(viewModel, requestScope())
		}
	}


	//endregion

	//region render

	private fun observeState() {
		viewModel.state
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { render(it) }
	}

	private fun render(state: PersonDetailsState) {
		state.person?.let {
			showPersonDetails(it)
		}
	}

	private fun showPersonDetails(person: Person) {
		textViewName?.text = person.name
		textViewNotifiedOn?.text = person.notifiedOn
		if (person.nationality == null) {
			textViewNationality?.visibility = View.GONE
			textViewLabelNationality?.visibility = View.GONE
		} else {
			textViewNationality?.visibility = View.VISIBLE
			textViewNationality?.text = person.nationality
		}
		if (person.countryOfResidence == null) {
			textViewCountryOfResidence?.visibility = View.GONE
			textViewLabelCountryOfResidence?.visibility = View.GONE
		} else {
			textViewCountryOfResidence?.visibility = View.VISIBLE
			textViewCountryOfResidence?.text = person.countryOfResidence
		}
		person.dateOfBirth?.let {
			textViewDateOfBirth?.visibility = View.VISIBLE
			textViewDateOfBirth?.text = "${it.month.toString()} / ${it.year.toString()}"
		} ?: run {
			textViewDateOfBirth?.visibility = View.GONE
			textViewLabelDateOfBirth?.visibility = View.GONE
		}
		val naturesOfControl = StringBuilder()
		for (i in person.naturesOfControl.indices) {
			naturesOfControl.append(person.naturesOfControl[i])
			if (i < person.naturesOfControl.size - 1) {
				naturesOfControl.append("; ")
			}
		}
		textViewNaturesOfControl?.text = naturesOfControl.toString()

		textViewAddressLine1?.text = person.address?.addressLine1
		textViewLocality?.text = person.address?.locality
		textViewPostalCode?.text = person.address?.postalCode
		person.identification?.placeRegistered?.let {
			textViewPlaceRegistered?.text = it
			textViewPlaceRegistered?.visibility = View.VISIBLE
		} ?: run {
			textViewPlaceRegistered?.visibility = View.GONE
			textViewLabelPlaceRegistered?.visibility = View.GONE
		}
		person.identification?.registrationNumber?.let {
			textViewRegistrationNumber?.text = it
			textViewRegistrationNumber?.visibility = View.VISIBLE
		} ?: run {
			textViewLabelRegistrationNumber?.visibility = View.GONE
			textViewRegistrationNumber?.visibility = View.GONE
		}
		if (person.address?.region == null) {
			textViewRegion?.visibility = View.GONE
		} else {
			textViewRegion?.visibility = View.VISIBLE
			textViewRegion?.text = person.address?.region
		}
		person.address?.country?.let {
			textViewCountry?.text = it
			textViewCountry?.visibility = View.VISIBLE
		} ?: run {
			textViewCountry?.visibility = View.GONE
		}
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
	}

	//endregion
}

fun Context.createPersonDetailsIntent(person: Person): Intent {
	return Intent(this, PersonDetailsActivity::class.java)
			.putExtra(PERSON_ITEM, person)
}
